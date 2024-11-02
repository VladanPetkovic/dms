export async function getDocuments(query = "") {
    const url = query ? `/api/documents/search?query=${query}` : '/api/documents';
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

export async function deleteDocument(id) {
    const response = await fetch(`/api/documents/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete document');
}
