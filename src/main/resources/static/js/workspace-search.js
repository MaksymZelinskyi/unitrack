function setupLiveSearchPanel({
    inputEl,
    resultsContainerId,
    endpoint,
    minLength = 2,
    debounceMs = 300,
    renderItem,
    emptyMessage = "No results found.",
    promptMessage = "Start typing to search…"
}) {
    const resultsContainer = document.getElementById(resultsContainerId);

    if (!inputEl || !resultsContainer) {
        console.error(`Live search setup failed: missing element(s) for "${resultsContainerId}"`);
        return;
    }

    let debounceTimer = null;
    let currentController = null; // cancels stale requests

    const renderResults = (data) => {
        resultsContainer.innerHTML = "";

        if (!data || data.length === 0) {
            resultsContainer.innerHTML = `<p class="empty-text">${emptyMessage}</p>`;
            return;
        }

        data.content.forEach(item => {
            const card = renderItem(item);
            resultsContainer.appendChild(card);
        });
    };

    const runSearch = async () => {
        const query = inputEl.value.trim();

        if (query.length < minLength) {
            resultsContainer.innerHTML = `<p class="empty-text">${promptMessage}</p>`;
            return;
        }

        // cancel any in-flight request so slow responses can't overwrite newer ones
        if (currentController) currentController.abort();
        currentController = new AbortController();

        resultsContainer.classList.add("is-loading");

        try {
            const response = await fetch(
                `${endpoint}?query=${encodeURIComponent(query)}`,
                { signal: currentController.signal }
            );
            if (!response.ok) throw new Error("Network error");

            const data = await response.json();
            renderResults(data);
        } catch (error) {
            if (error.name !== "AbortError") {
                console.error(`Error fetching results from ${endpoint}:`, error);
                resultsContainer.innerHTML = `<p class="error-text">Something went wrong. Please try again.</p>`;
            }
        } finally {
            resultsContainer.classList.remove("is-loading");
        }
    };

    inputEl.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(runSearch, debounceMs);
    });

    // submitting the form (Enter key) runs the search immediately, no page navigation
    const form = inputEl.closest("form");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            clearTimeout(debounceTimer);
            runSearch();
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".live-search-input").forEach(input => {
        setupLiveSearchPanel({
            inputEl: input,
            resultsContainerId: input.dataset.results,
            endpoint: input.dataset.endpoint,
            minLength: parseInt(input.dataset.minLength, 10) || 2,
            debounceMs: parseInt(input.dataset.debounce, 10) || 300,
            renderItem: (workspace) => {
                const card = document.createElement("a");
                card.className = "project-card";
                card.href = `/workspaces/${workspace.id}`;

                card.innerHTML = `
                    <h3>${workspace.name}</h3>
                    <p>${workspace.description ?? ""}</p>
                   <!-- <span class="meta">${workspace.memberCount ?? 0} members</span> -->
                `;
                return card;
            }
        });
    });
});