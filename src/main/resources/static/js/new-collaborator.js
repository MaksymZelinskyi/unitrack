(function () {
      const picker = document.getElementById('projectPicker');
      const list = document.getElementById('selected-list');
      const placeholder = document.getElementById('no-projects');

      picker.addEventListener('change', function () {
        const id = this.value;
        if (!id) return;
        const name = this.options[this.selectedIndex].text;
        placeholder?.remove();

        if (list.querySelector(`[data-id="${CSS.escape(id)}"]`)) {
          this.value = '';
          return;
        }

        const row = document.createElement('div');
        row.className = 'project-row';
        row.dataset.id = id;
        row.innerHTML = `
          <button type="button" class="remove-btn" title="Remove">×</button>
          <span class="project-name">${name}</span>
          <input type="hidden" name="selected[0].id" value="${id}">
          <select name="selected[0].role" class="role-select">
            <option value="BACKEND_DEV" selected>Backend Dev</option>
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
        reindex();
        this.value = '';
      });

      list.addEventListener('click', function (e) {
        if (e.target.closest('.remove-btn')) {
          const row = e.target.closest('.project-row');
          row?.remove();
          reindex();
          if (!list.children.length) {
            list.innerHTML = '<p id="no-projects" class="placeholder">No projects selected yet.</p>';
          }
        }
      });

      function reindex() {
        list.querySelectorAll('.project-row').forEach((row, i) => {
          const idInput = row.querySelector('input[type="hidden"]');
          const roleSel = row.querySelector('select');
          if (idInput) idInput.name = `selected[${i}].id`;
          if (roleSel) roleSel.name = `selected[${i}].role`;
        });
      }
    })();