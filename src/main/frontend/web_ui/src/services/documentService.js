export async function getDocuments(name = "", page = 0, maxCountDocuments = 10) {
    let url = `/api/documents?page=${page}&maxCountDocuments=${maxCountDocuments}`;
    if (name) {
        url = url + `&name=${name}`;
    }
    const response = await fetch(url);
    if (!response.ok) throw new Error('Failed to fetch documents');
    return await response.json();
}

export async function uploadDocument(formData) {
    const response = await fetch('/api/documents', {
        method: 'POST',
        body: formData,
    });
    if (!response.ok) throw new Error('Failed to upload document');
}

export async function updateDocumentById(id, updatedData) {
    const response = await fetch(`/api/documents/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
    });

    if (response.ok) {
        return await response.json(); // Return the updated document data
    } else if (response.status === 404) {
        throw new Error('Document not found');
    } else {
        throw new Error('Failed to update document');
    }
}

export async function deleteDocument(id) {
    const response = await fetch(`/api/documents/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete document');
}

export async function downloadDocument(id) {
    // TODO: implement
    return [];
}
