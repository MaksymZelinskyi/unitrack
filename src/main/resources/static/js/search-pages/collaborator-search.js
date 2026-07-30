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
            const row = document.createElement("div");
            row.className = "collaborator-row";

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

            const inviteBtn = document.createElement("button");
            inviteBtn.type = "button";
            inviteBtn.className = "invite-button invite-button-small";
            inviteBtn.textContent = "Invite";
            inviteBtn.addEventListener("click", (event) => {
                event.preventDefault();
                event.stopPropagation();
                inviteCollaborator(collaborator, inviteBtn);
            });

            row.appendChild(card);
            row.appendChild(inviteBtn);
            return row;
        },
        emptyMessage: "No collaborators match your search.",
        promptMessage: "Start typing to search collaborators…"
    });
});

function inviteCollaborator(collaborator, buttonEl) {
    buttonEl.disabled = true;
    buttonEl.textContent = "Inviting…";

    fetch(`/workspaces/${workspace.id}/invite?collaboratorId=${collaborator.id}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
        .then((res) => {
            if (!res.ok) throw new Error("Invite failed");
            buttonEl.textContent = "Invited";
        })
        .catch(() => {
            buttonEl.disabled = false;
            buttonEl.textContent = "Invite";
        });
}