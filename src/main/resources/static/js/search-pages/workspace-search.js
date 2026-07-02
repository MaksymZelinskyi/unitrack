document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("workspaceQuery");
    if (!input) return; // this page doesn't have the workspace search box

    setupLiveSearchPanel({
        inputEl: input,
        resultsContainerId: input.dataset.results,
        endpoint: input.dataset.endpoint,
        minLength: parseInt(input.dataset.minLength, 10) || 2,
        debounceMs: parseInt(input.dataset.debounce, 10) || 300,
        renderItem: (workspace) => {
            const card = document.createElement("a");
            card.className = "workspace-in-list";
            card.href = `/workspaces/${workspace.id}`;
            card.innerHTML = `
                <h3>${workspace.name}</h3>
                <p>${workspace.description ?? ""}</p>
                <span class="meta">${workspace.memberCount ?? 0} members</span>
            `;
            return card;
        },
        emptyMessage: "No workspaces match your search.",
        promptMessage: "Start typing to search workspaces…"
    });
});