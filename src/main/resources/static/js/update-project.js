function addAssignee(selectEl) {
    const list = document.getElementById("assignees-list");
    const id = selectEl.value;
    if (!id) return;

    const name = selectEl.options[selectEl.selectedIndex].text;

    if (list.querySelector(`[data-id="${CSS.escape(id)}"]`)) {
        selectEl.value = "";
        return;
    }

    const row = document.createElement("div");
    row.className = "assignee-row";
    row.dataset.id = id;

    row.innerHTML = `
        <button type="button" class="remove-btn" aria-label="Remove">✕</button>
        <span class="assignee-name">${name}</span>

        <input type="hidden" name="assignees[0].id" value="${id}">

        <select name="assignees[0].role" class="role-picker">
        <option value="BACKEND_DEV">Backend Dev</option>
        <option value="FRONTEND_DEV">Frontend Dev</option>
        <option value="FULLSTACK_DEV">Fullstack Dev</option>
        <option value="TESTER">Tester</option>
        <option value="DEVOPS">DevOps</option>
        <option value="DESIGNER">Designer</option>
        <option value="PRODUCT_OWNER">Product Owner</option>
        <option value="INTERN">Intern</option>
        <option value="PROJECT_MANGER">Project Manager</option>
        </select>
    `;

    list.appendChild(row);
    reindexAssignees();
    selectEl.value = "";
}

function reindexAssignees() {
    const rows = document.querySelectorAll(".assignee-row");
    rows.forEach((row, i) => {
        row.querySelector('input[type="hidden"]').name = `assignees[${i}].id`;
        row.querySelector("select").name = `assignees[${i}].role`;
    });
}

document.addEventListener("click", e => {
    if (e.target.closest(".remove-btn")) {
        e.target.closest(".assignee-row").remove();
        reindexAssignees();
    }
});