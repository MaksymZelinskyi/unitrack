document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("collaboratorQuery");
    if (!input) return; // this page doesn't have the collaborator search box

    setupLiveSearchPanel({
        inputEl: input,
        resultsContainerId: input.dataset.results,
        endpoint: input.dataset.endpoint,
        minLength: parseInt(input.dataset.minLength, 10) || 1,
        debounceMs: parseInt(input.dataset.debounce, 10) || 300,
        renderItem: (collaborator) => {
            const card = document.createElement("a");
            card.className = "workspace-card collaborator-card";
            card.href = `/collaborators/${collaborator.id}`;

            const img = document.createElement("img");
            img.src = collaborator.avatarUrl || "/icons/profile-icon.png";
            img.alt = `${collaborator.name}`;
            img.className = "suggestion-avatar";
            img.onerror = () => { img.src = "/icons/error.webp"; };

            const name = document.createElement("h3");
            name.textContent = `${collaborator.name}`;

            card.appendChild(img);
            card.appendChild(name);
            return card;
        },
        emptyMessage: "No collaborators match your search.",
        promptMessage: "Start typing to search collaborators…"
    });
});