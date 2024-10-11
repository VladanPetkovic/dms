fetch('/api/documents')
    .then(response => response.json())
    .then(data => {
        document.getElementById('data').innerText = JSON.stringify(data);
    })
    .catch(error => {
        document.getElementById('data').innerText = "Error loading data";
    });
