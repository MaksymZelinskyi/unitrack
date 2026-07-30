/**
 * Generic live search panel.
 * Renders full result cards into a results container as the user types
 * (instead of a small dropdown). Supports paginated (Page<T>) responses.
 *
 * @param {Object} config
 * @param {HTMLInputElement} config.inputEl - the text input element
 * @param {string} config.resultsContainerId - id of the container to render results into
 * @param {string} config.endpoint - URL to fetch results from (expects ?query=&page=)
 * @param {number} [config.minLength=2] - minimum query length before searching
 * @param {number} [config.debounceMs=300] - debounce delay in ms
 * @param {(item: any) => Node} config.renderItem - returns a DOM node (e.g. a card) for one result
 * @param {string} [config.emptyMessage] - shown when a search returns no results
 * @param {string} [config.promptMessage] - shown before the user has typed enough characters
 */
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
    let currentController = null; // cancels stale in-flight requests

    const renderResults = (items, pageData) => {
        resultsContainer.innerHTML = "";

        if (!items || items.length === 0) {
            resultsContainer.innerHTML = `<p class="empty-text">${emptyMessage}</p>`;
            return;
        }

        const list = document.createElement("div");
        list.className = "results-list";
        items.forEach(item => list.appendChild(renderItem(item)));
        resultsContainer.appendChild(list);

        if (pageData && pageData.totalPages > 1) {
            const pagination = document.createElement("div");
            pagination.className = "pagination";
            pagination.innerHTML = `
                <button ${pageData.first ? "disabled" : ""} data-page="${pageData.number - 1}">Prev</button>
                <span>Page ${pageData.number + 1} of ${pageData.totalPages}</span>
                <button ${pageData.last ? "disabled" : ""} data-page="${pageData.number + 1}">Next</button>
            `;
            pagination.querySelectorAll("button").forEach(btn => {
                btn.addEventListener("click", () => runSearch(parseInt(btn.dataset.page, 10)));
            });
            resultsContainer.appendChild(pagination);
        }
    };

    const runSearch = async (page = 0) => {
        const query = inputEl.value.trim();

        if (query.length < minLength) {
            resultsContainer.innerHTML = `<p class="empty-text">${promptMessage}</p>`;
            return;
        }

        if (currentController) currentController.abort();
        currentController = new AbortController();

        resultsContainer.classList.add("is-loading");

        try {
            const response = await fetch(
                `${endpoint}?query=${encodeURIComponent(query)}&page=${page}`,
                { signal: currentController.signal }
            );
            if (!response.ok) throw new Error("Network error");

            const data = await response.json();
            const items = data.content ?? data;
            renderResults(items, data.content ? data : null);
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
        debounceTimer = setTimeout(() => runSearch(0), debounceMs);
    });

    // Enter re-runs the search immediately instead of submitting/navigating the form
    const form = inputEl.closest("form");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            clearTimeout(debounceTimer);
            runSearch(0);
        });
    }

    // initial state
    resultsContainer.innerHTML = `<p class="empty-text">${promptMessage}</p>`;
}