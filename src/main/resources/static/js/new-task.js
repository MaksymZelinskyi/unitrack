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
        <button type="button" class="remove-btn">✕</button>
        <span class="assignee-name">${name}</span>
        <input type="hidden" name="assignees[0].id" value="${id}">
      `;

      list.appendChild(row);
      reindexAssignees();
      selectEl.value = '';
}

function reindexAssignees() {
      const list = document.getElementById('assignees-list');
      list.querySelectorAll('.assignee').forEach((row, i) => {
        const idInput = row.querySelector('input[type="hidden"]');
        if (idInput) idInput.name = `assignees[${i}].id`;
      });
}

document.addEventListener('click', e => {
      if (e.target.closest('.remove-btn')) {
        const row = e.target.closest('.assignee');
        row.remove();
        reindexAssignees();
      }
});