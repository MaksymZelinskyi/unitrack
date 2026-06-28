/**
 * Generic dropdown autocomplete.
 * Renders a list of clickable suggestions under an input as the user types.
 *
 * @param {Object} config
 * @param {HTMLInputElement} config.inputEl - the text input element
 * @param {string} config.suggestionsId - id of the <ul>/<div> to render suggestions into
 * @param {string} config.endpoint - URL to fetch suggestions from (expects ?query=)
 * @param {number} [config.minLength=2] - minimum query length before searching
 * @param {number} [config.debounceMs=0] - debounce delay in ms
 * @param {(item: any) => (string|Node)} [config.renderItem] - returns the content for one suggestion;
 *        return a string for plain text, or a Node/DocumentFragment for custom markup (e.g. avatar + name)
 * @param {(item: any, inputEl: HTMLInputElement) => void} [config.onSelect] - called when a suggestion is clicked;
 *        if omitted, defaults to filling the input with item.name
 */
function setupAutocomplete({
    inputEl,
    suggestionsId,
    endpoint,
    minLength = 2,
    debounceMs = 0,
    renderItem,
    onSelect
}) {
    const searchBox = inputEl;
    const suggestions = document.getElementById(suggestionsId);

    if (!searchBox || !suggestions) {
        console.error(`Autocomplete setup failed: missing suggestions element "${suggestionsId}"`);
        return;
    }

    let debounceTimer = null;
    let currentController = null; // cancels stale in-flight requests

    const handleInput = async () => {
        const query = searchBox.value.trim();

        if (query.length < minLength) {
            suggestions.innerHTML = "";
            return;
        }

        if (currentController) currentController.abort();
        currentController = new AbortController();

        try {
            const response = await fetch(
                `${endpoint}?query=${encodeURIComponent(query)}`,
                { signal: currentController.signal }
            );
            if (!response.ok) throw new Error("Network error");

            const data = await response.json();
            const items = data.content ?? data; // unwrap Page<T> if paginated, else use array as-is

            suggestions.innerHTML = "";

            items.forEach(item => {
                const li = document.createElement("li");
                const content = renderItem ? renderItem(item) : item.name;

                if (content instanceof Node) {
                    li.appendChild(content);
                } else {
                    li.textContent = content;
                }

                li.addEventListener("click", () => {
                    if (onSelect) {
                        onSelect(item, searchBox);
                    } else {
                        searchBox.value = item.name;
                    }
                    suggestions.innerHTML = "";
                });

                suggestions.appendChild(li);
            });
        } catch (error) {
            if (error.name !== "AbortError") {
                console.error(`Error fetching suggestions from ${endpoint}:`, error);
            }
        }
    };

    searchBox.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(handleInput, debounceMs);
    });

    // close dropdown when clicking outside the input/suggestions
    document.addEventListener("click", (e) => {
        if (!searchBox.contains(e.target) && !suggestions.contains(e.target)) {
            suggestions.innerHTML = "";
        }
    });

    // close dropdown on Escape
    searchBox.addEventListener("keydown", (e) => {
        if (e.key === "Escape") suggestions.innerHTML = "";
    });
}