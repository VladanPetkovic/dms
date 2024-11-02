import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import UploadForm from './components/UploadForm';
import SearchBar from './components/SearchBar';
import DocumentTable from './components/DocumentTable';
import { getDocuments, uploadDocument, deleteDocument } from './services/documentService';

function App() {
    const [documents, setDocuments] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        fetchDocuments();
    }, [searchQuery]);

    const fetchDocuments = async () => {
        try {
            const data = await getDocuments(searchQuery);
            setDocuments(data);
        } catch (error) {
            console.error('Error fetching documents:', error);
        }
    };

    const handleSearch = (query) => {
        setSearchQuery(query);
    };

    const handleUpload = async (formData) => {
        try {
            await uploadDocument(formData);
            fetchDocuments();
        } catch (error) {
            console.error('Error uploading document:', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteDocument(id);
            fetchDocuments();
        } catch (error) {
            console.error('Error deleting document:', error);
        }
    };

    return (
        <div className="app-container">
            <h1>Document Management System</h1>
            <UploadForm onUpload={handleUpload} />
            <SearchBar onSearch={handleSearch} />
            <DocumentTable documents={documents} onDelete={handleDelete} />
        </div>
    );
}

ReactDOM.render(<App />, document.getElementById('root'));
