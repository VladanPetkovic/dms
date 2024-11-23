import React, {useState} from 'react';
import '../style/index.css';
import downloadIcon from '../icons/download.png';

function DocumentTable({documents, onDelete, onUpdate, onDownload}) {
    const [editId, setEditId] = useState(null);
    const [editData, setEditData] = useState(null);

    const handleEditClick = (doc) => {
        setEditId(doc.id);
        setEditData({...doc}); // Initialize with entire document
    };

    const handleSaveClick = (id) => {
        onUpdate(id, editData);
        setEditId(null);
    };

    const handleCancelClick = () => {
        setEditId(null);
    }

    const handleChange = (e) => {
        const {name, value} = e.target;
        setEditData((prev) => ({ ...prev, [name]: value }));
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
                        <td>{new Date(doc.createdAt).toLocaleString()}</td>
                        <td>{doc.updatedAt == null ? "-" : new Date(doc.updatedAt).toLocaleString()}</td>
                        <td>
                            <div className="action-buttons">
                                {editId === doc.id ? (
                                    <div>
                                        <button className="save-button" onClick={() => handleSaveClick(doc.id)}>Save
                                        </button>
                                        <button className="cancel-button"
                                                onClick={() => handleCancelClick()}>Cancel
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
