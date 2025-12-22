const sections = document.querySelectorAll(".sortable");

sections.forEach(section => {
    new Sortable(section, {
        group: "shared", //allows moving between lists
        animation: 150,
        onAdd: async function (evt) {
            let taskId = evt.item.dataset.id;
            let newSection = evt.to.id;

            //Send PUT request
            await fetch(`/tasks/${taskId}/status`, {
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ newStatus: newSection })
            });
            window.location.reload();
       }
    });
});
