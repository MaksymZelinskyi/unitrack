function addAssignee(selectEl) {
      const list = document.getElementById('assignees-list');
      const id = selectEl.value;
      if (!id) return;

      const name = selectEl.options[selectEl.selectedIndex].text;

      if (list.querySelector(`[data-id="${CSS.escape(id)}"]`)) {
        selectEl.value = '';
        return;
      }

      const row = document.createElement('div');
      row.className = 'assignee';
      row.dataset.id = id;

      row.innerHTML = `
        <button type="button" class="remove-btn" aria-label="Remove">✕</button>
        <span class="assignee-name">${name}</span>
        <input type="hidden" name="assignees[0].id" value="${id}">
        <select name="assignees[0].role" class="role-select">
          <option value="BACKEND_DEV" selected>Backend Dev</option>
          <option value="FRONTEND_DEV">Frontend Dev</option>
          <option value="FULLSTACK_DEV">Fullstack Dev</option>
          <option value="TESTER">Tester</option>
          <option value="DEVOPS">DevOps</option>
          <option value="DESIGNER">Designer</option>
          <option value="PRODUCT_OWNER">Product Owner</option>
          <option value="INTERN">Intern</option>
        </select>
      `;

      list.appendChild(row);
      reindexAssignees();
      selectEl.value = '';
}

function reindexAssignees() {
      const list = document.getElementById('assignees-list');
      list.querySelectorAll('.assignee').forEach((row, i) => {
        const idInput = row.querySelector('input[type="hidden"]');
        const roleSel = row.querySelector('select');
        if (idInput) idInput.name = `assignees[${i}].id`;
        if (roleSel) roleSel.name = `assignees[${i}].role`;
      });
}

document.addEventListener('click', e => {
      if (e.target.closest('.remove-btn')) {
        const row = e.target.closest('.assignee');
        row.remove();
        reindexAssignees();
      }
});

const form = document.getElementById('projectForm')
const start = document.getElementById('start');
const deadline = document.getElementById('deadline')
const errorSpan = document.getElementById('error');

form.addEventListener('submit', (event) => {
    if (new Date(start.value) > new Date(deadline.value)) {
        event.preventDefault();
        errorSpan.style.display = 'inline';
    } else {
        errorSpan.style.display = 'none';
    }
});
