const form = document.getElementById('collaboratorForm');
    const emailInput = document.getElementById('email');
    const errorSpan = document.getElementById('error');

    form.addEventListener('submit', (event) => {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test(emailInput.value)) {
        event.preventDefault();
        errorSpan.style.display = 'inline';
      } else {
        errorSpan.style.display = 'none';
      }
    });