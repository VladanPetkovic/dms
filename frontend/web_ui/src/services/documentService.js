const BASE_URL = 'http://localhost:8081/documents';

export async function getDocuments(name = "", page = 0, maxCountDocuments = 10) {
    let url = `${BASE_URL}?page=${page}&maxCountDocuments=${maxCountDocuments}`;
    if (name) {
        url += `&name=${name}`;
    }
    const response = await fetch(url);
    if (!response.ok) throw new Error('Failed to fetch documents');
    return await response.json();
}

export async function searchDocuments(searchTerm, page = 0, maxCountDocuments = 10) {
    let url = `${BASE_URL}/search?search=${searchTerm}?page=${page}&maxCountDocuments=${maxCountDocuments}`;
    const response = await fetch(url);
    console.log(response);
    if (!response.ok) throw new Error('Failed to search for documents');
    return await response.json();
}

export async function uploadDocument(formData) {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        body: formData,
        headers: {
            'Accept': 'application/json',
        },
    });
    if (response.status === 201) {
        return await response.json();
    } else if (response.status === 400) {
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
    try {
        const response = await fetch(`${BASE_URL}/${id}/download`, {
            method: 'GET',
            headers: {
                'Accept': 'application/octet-stream',
            },
        });

        if (!response.ok) {
            throw new Error('Document not found or failed to download');
        }

        const contentDisposition = response.headers.get('Content-Disposition');
        const fileName = contentDisposition
            ? contentDisposition.split('filename=')[1].replace(/"/g, '')
            : 'downloaded-file';

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Error downloading document:', error);
        throw error;
    }
}
