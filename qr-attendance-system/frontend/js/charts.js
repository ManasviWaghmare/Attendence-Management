const initAttendanceChart = (ctx) => {
    const data = {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        datasets: [{
            label: 'Attendance Rate (%)',
            data: [82, 94, 78, 88, 92, 85],
            fill: true,
            borderColor: '#6366f1',
            backgroundColor: 'rgba(99, 102, 241, 0.1)',
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: '#6366f1',
            borderWidth: 3
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1e293b',
                    titleColor: '#f8fafc',
                    bodyColor: '#94a3b8',
                    padding: 12,
                    displayColors: false,
                    cornerRadius: 12
                }
            },
            scales: {
                x: { 
                    grid: { display: false },
                    ticks: { color: '#94a3b8' }
                },
                y: { 
                    min: 0,
                    max: 100,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: '#94a3b8' }
                }
            }
        }
    };

    return new Chart(ctx, config);
};
