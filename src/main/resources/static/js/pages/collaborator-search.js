document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("collaboratorQuery");
    if (!input) return; // this page doesn't have the collaborator search box

    setupAutocomplete({
        inputEl: input,
        suggestionsId: input.dataset.suggestions,
        endpoint: input.dataset.endpoint,
        minLength: parseInt(input.dataset.minLength, 10) || 1,
        debounceMs: parseInt(input.dataset.debounce, 10) || 300,
        renderItem: (collaborator) => {
            const img = document.createElement("img");
            img.src = collaborator.avatarUrl || "/icons/profile-icon.png";
            img.alt = collaborator.name;
            img.className = "suggestion-avatar";
            img.onerror = () => { img.src = "/icons/error.png"; };

            const label = document.createElement("span");
            label.className = "suggestion-label";
            label.textContent = collaborator.name;

            const fragment = document.createDocumentFragment();
            fragment.appendChild(img);
            fragment.appendChild(label);
            return fragment;
        },
        onSelect: (collaborator, searchBox) => {
            searchBox.value = collaborator.name;
            const hiddenId = document.getElementById("collaboratorId");
            if (hiddenId) hiddenId.value = collaborator.id;
        }
    });
});