// ========== HELPERS ==========
function show(el) {
    el.classList.remove("hidden");
}
function hide(el) {
    el.classList.add("hidden");
}

// ========== REPLY HANDLER ==========
document.querySelectorAll(".reply-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        const li = btn.closest("li");
        const form = li.querySelector(".comment-reply-form");

        // Hide all other reply forms
        document.querySelectorAll(".comment-reply-form").forEach(hide);

        show(form);
        form.querySelector("input[name='text']").focus();
    });
});

// ========== REPLY CANCEL ==========
document.querySelectorAll(".comment-reply-form .cancel-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        hide(btn.closest(".comment-reply-form"));
    });
});

// ========== EDIT MODE ==========
document.querySelectorAll(".edit-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        const card = btn.closest(".comment-card");
        const text = card.querySelector(".comment-text");
        const form = card.querySelector(".comment-edit-form");

        // Hide all other edit forms
        document.querySelectorAll(".comment-edit-form").forEach(hide);

        hide(text);
        show(form);
        form.querySelector("textarea").focus();
    });
});

// ========== EDIT CANCEL ==========
document.querySelectorAll(".comment-edit-form .cancel-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        const form = btn.closest(".comment-edit-form");
        const card = form.closest(".comment-card");
        const text = card.querySelector(".comment-text");

        hide(form);
        show(text);
    });
});

// ========== AUTO SCROLL TO COMMENT ==========
document.querySelectorAll(".reply-ref").forEach(link => {
    link.addEventListener("click", (e) => {
        const id = link.getAttribute("href");
        const target = document.querySelector(id);

        if (target) {
            e.preventDefault();
            target.scrollIntoView({ behavior: "smooth", block: "center" });
            target.classList.add("highlight");

            // remove highlight after a second
            setTimeout(() => target.classList.remove("highlight"), 1000);
        }
    });
});
