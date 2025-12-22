//Users chart
new Chart(document.getElementById('userActivityChart'), {
    type: 'bar',
    data: {
        labels: usernames,
        datasets: [{
            label: 'Tasks Completed',
            data: tasksPerUser,
            backgroundColor: 'rgba(54,162,235,0.6)',
            borderColor: 'rgba(54,162,235,1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true } }
    }
});

//Projects chart
new Chart(document.getElementById('projectActivityChart'), {
    type: 'doughnut',
    data: {
        labels: projects,
        datasets: [{
            data: tasksPerProject,
            backgroundColor: [
                'rgba(75,192,192,0.6)',
                'rgba(255,159,64,0.6)',
                'rgba(153,102,255,0.6)'
            ],
            borderColor: '#fff',
            borderWidth: 2
        }]
    },
    options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
});