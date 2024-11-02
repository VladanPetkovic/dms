import React, {useState} from 'react';
import '../style/index.css';
import downloadIcon from '../icons/download.png';

function DocumentTable({documents, onDelete, onUpdate, onDownload}) {
    const [editId, setEditId] = useState(null); // Track the ID of the document being edited
    const [editData, setEditData] = useState({name: '', description: ''}); // Store edited values temporarily

    const handleEditClick = (doc) => {
        setEditId(doc.id);
        setEditData({name: doc.name, description: doc.description}); // Initialize with current values
    };

    const handleSaveClick = (id) => {
        onUpdate(id, editData);
        setEditId(null);
    };

    const handleCancelClick = (id) => {
        setEditId(null);
    }

    const handleChange = (e) => {
        const {name, value} = e.target;
        setEditData((prev) => ({...prev, [name]: value})); // Update editData state
    };

    return (
        <div className="document-table-container">
            <table className="document-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Created At</th>
                    <th>Updated At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {documents.map((doc) => (
                    <tr key={doc.id}>
                        <td>{doc.id}</td>

                        {/* Check if the current row is in edit mode */}
                        <td>
                            {editId === doc.id ? (
                                <div className="search-bar">
                                    <input
                                        type="text"
                                        name="name"
                                        value={editData.name}
                                        onChange={handleChange}
                                    />
                                </div>
                            ) : (
                                doc.name
                            )}
                        </td>
                        <td>
                            {editId === doc.id ? (
                                <div className="search-bar">
                                    <input
                                        type="text"
                                        name="description"
                                        value={editData.description}
                                        onChange={handleChange}
                                    />
                                </div>
                            ) : (
                                doc.description
                            )}
                        </td>
                        <td>{new Date(doc.created_at).toLocaleString()}</td>
                        <td>{doc.updated_at == null ? "-" : new Date(doc.updated_at).toLocaleString()}</td>
                        <td>
                            <div className="action-buttons">
                                {editId === doc.id ? (
                                    <div>
                                        <button className="save-button" onClick={() => handleSaveClick(doc.id)}>Save
                                        </button>
                                        <button className="cancel-button"
                                                onClick={() => handleCancelClick(doc.id)}>Cancel
                                        </button>
                                    </div>
                                ) : (
                                    <button className="update-button" onClick={() => handleEditClick(doc)}>Edit</button>
                                )}
                                <button className="delete-button" onClick={() => onDelete(doc.id)}>Delete</button>
                                <div className="download-button" onClick={() => onDownload(doc.id)}>
                                    <img src={downloadIcon} alt="Download" className="icon"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default DocumentTable;
