document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".confirm-delete").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();

            const form = this.closest("form");

            showConfirmation("Are you sure you want to delete this?", (confirmed) => {
                if (confirmed) form.submit();
            });
        });
    });
});

function showConfirmation(message, callback) {
    const modal = document.getElementById("confirm-modal");
    const msgEl = modal.querySelector(".confirm-message");
    const yesBtn = document.getElementById("confirm-yes");
    const noBtn = document.getElementById("confirm-no");

    msgEl.textContent = message;
    modal.classList.remove("hidden");

    // YES
    yesBtn.onclick = () => {
        modal.classList.add("hidden");
        callback(true);
    };

    // NO
    noBtn.onclick = () => {
        modal.classList.add("hidden");
        callback(false);
    };
}
