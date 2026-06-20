function setupAutocomplete({
    inputId,
    suggestionsId,
    endpoint,
    minLength = 2,
    labelField = "name",
    debounceMs = 0,
    onSelect = null
}) {
    const searchBox = document.getElementById(inputId);
    const suggestions = document.getElementById(suggestionsId);

    if (!searchBox || !suggestions) {
        console.error(`Autocomplete setup failed: missing element(s) "${inputId}" or "${suggestionsId}"`);
        return;
    }

    const getLabel = (item) => item[labelField];
    let debounceTimer = null;

    const handleInput = async () => {
        const query = searchBox.value.trim();

        if (query.length <= minLength) {
            suggestions.innerHTML = "";
            return;
        }

        try {
            const response = await fetch(`${endpoint}?query=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error("Network error");

            const data = await response.json();
            suggestions.innerHTML = "";

            data.forEach(item => {
                const div = document.createElement("div");
                div.textContent = getLabel(item);
                div.addEventListener("click", () => {
                    if (onSelect) {
                        onSelect(item, searchBox);
                    } else {
                        searchBox.value = getLabel(item);
                    }
                    suggestions.innerHTML = "";
                });
                suggestions.appendChild(div);
            });
        } catch (error) {
            console.error(`Error fetching suggestions from ${endpoint}:`, error);
        }
    };

    searchBox.addEventListener("input", () => {
        if (debounceMs > 0) {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(handleInput, debounceMs);
        } else {
            handleInput();
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".autocomplete-input").forEach(input => {
        setupAutocomplete({
            inputId: input.id,
            suggestionsId: input.dataset.suggestions,
            endpoint: input.dataset.endpoint,
            minLength: parseInt(input.dataset.minLength, 10) || 2,
            labelField: input.dataset.labelField || "name",
            debounceMs: parseInt(input.dataset.debounce, 10) || 0
        });
    });
});