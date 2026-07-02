document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("skillQuery");
    if (!input) return; // this page doesn't have the skill search box

    setupAutocomplete({
        inputEl: input,
        suggestionsId: input.dataset.suggestions,
        endpoint: input.dataset.endpoint,
        minLength: parseInt(input.dataset.minLength, 10) || 1,
        debounceMs: parseInt(input.dataset.debounce, 10) || 0,
        renderItem: (skill) => skill.name,
        onSelect: (skill, searchBox) => {
            searchBox.value = skill.name;
            const hiddenId = document.getElementById("skillId");
            if (hiddenId) hiddenId.value = skill.id;
        }
    });
});