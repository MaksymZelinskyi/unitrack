document.addEventListener("DOMContentLoaded", () => {
    const searchBox = document.getElementById("searchQuery");
    const suggestions = document.getElementById("suggestions");

    searchBox.addEventListener("input", async () => {
        const query = searchBox.value.trim();

        if (query.length > 1) {
            try {
                const response = await fetch(`/skills/search?searchQuery=${encodeURIComponent(query)}`);
                if (!response.ok) throw new Error("Network error");

                const data = await response.json();
                suggestions.innerHTML = "";

                data.forEach(item => {
                    const div = document.createElement("div");
                    div.textContent = item.name;
                    div.addEventListener("click", () => {
                        searchBox.value = item.name;
                        suggestions.innerHTML = "";
                    });
                    suggestions.appendChild(div);
                });
            } catch (error) {
                console.error("Error fetching suggestions:", error);
            }
        } else {
            suggestions.innerHTML = "";
        }
    });
});