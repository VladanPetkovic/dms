const BASE_URL = 'http://localhost:8081/api/documents'; // Set the base URL to include the port

export async function getDocuments(name = "", page = 0, maxCountDocuments = 10) {
    let url = `${BASE_URL}?page=${page}&maxCountDocuments=${maxCountDocuments}`;
    if (name) {
        url += `&name=${name}`;
    }
    const response = await fetch(url);
    if (!response.ok) throw new Error('Failed to fetch documents');
    return await response.json();
}

export async function uploadDocument(formData) {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        body: formData,
    });
    if (response.ok) {
        return await response.json(); // Return the response if upload is successful
    } else if (response.status === 400) {
        console.log(response);
        throw new Error('Validation failed');
    } else {
        throw new Error('Failed to upload document');
    }
}

export async function updateDocumentById(id, updatedData) {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
    });

    if (response.ok) {
        return await response.json();
    } else if (response.status === 400) {
        throw new Error('Validation failed');
    } else if (response.status === 404) {
        throw new Error('Document not found');
    } else {
        throw new Error('Failed to update document');
    }
}

export async function deleteDocument(id) {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete document');
}

export async function downloadDocument(id) {
    // TODO: implement
    return [];
}
