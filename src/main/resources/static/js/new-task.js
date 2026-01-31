function addAssignee(selectEl) {
      const id = selectEl.value;
      const list = document.getElementById('selected-list');
      const placeholder = document.getElementById('no-assignees')
      if (!id) return;

      const name = selectEl.options[selectEl.selectedIndex].text;

      if (list.querySelector(`[data-id="${CSS.escape(id)}"]`)) {
        selectEl.value = '';
        return;
      }

      placeholder?.remove();

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
      const list = document.getElementById('selected-list');
      list.querySelectorAll('.assignee').forEach((row, i) => {
        const idInput = row.querySelector('input[type="hidden"]');
        if (idInput) idInput.name = `assignees[${i}].id`;
      });
}

const list = document.getElementById('selected-list');
document.addEventListener('click', e => {
      if (e.target.closest('.remove-btn')) {
        const row = e.target.closest('.assignee');
        row.remove();
        reindexAssignees();
        if (!list.children.length) {
             list.innerHTML = '<p id="no-assignees" class="placeholder">No assignees selected yet.</p>';
                  }
      }
});