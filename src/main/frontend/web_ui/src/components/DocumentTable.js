import React from 'react';
import '../style/index.css';

function DocumentTable({ documents, onDelete }) {
    return (
        <div className="document-table-container">
            <table className="document-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {documents.map((doc) => (
                    <tr key={doc.id}>
                        <td>{doc.id}</td>
                        <td>{doc.name}</td>
                        <td>{doc.description}</td>
                        <td>{new Date(doc.created_at).toLocaleString()}</td>
                        <td>
                            <button className="delete-button" onClick={() => onDelete(doc.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default DocumentTable;
