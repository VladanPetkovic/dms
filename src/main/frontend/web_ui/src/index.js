import React, {useState, useEffect} from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import UploadForm from './components/UploadForm';
import SearchBar from './components/SearchBar';
import DocumentTable from './components/DocumentTable';
import {
    getDocuments,
    uploadDocument,
    deleteDocument,
    updateDocumentById,
    downloadDocument
} from './services/documentService';
import Pagination from "./components/Pagination";

function App() {
    const [documents, setDocuments] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(0);

    useEffect(() => {
        fetchDocuments();
    }, [searchQuery, currentPage]);

    const fetchDocuments = async () => {
        try {
            const response = await getDocuments(searchQuery, currentPage);
            console.log(response);
            setDocuments(response.content);
            setTotalPages(response.totalPages);
        } catch (error) {
            console.error('Error fetching documents:', error);
        }
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    };

    const handleSearch = async (query) => {
        setSearchQuery(query);
        setCurrentPage(0);
    };

    const handleUpload = async (formData) => {
        try {
            await uploadDocument(formData);
            await fetchDocuments();
        } catch (error) {
            console.error('Error uploading document:', error);
        }
    };

    const handleUpdate = async (id, updatedData) => {
        try {
            await updateDocumentById(id, updatedData);
            await fetchDocuments();
        } catch (error) {
            console.error('Error updating document:', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteDocument(id);
            await fetchDocuments();
        } catch (error) {
            console.error('Error deleting document:', error);
        }
    };

    const handleDownload = async (id) => {
        try {
            await downloadDocument(id);
            console.log("TO BE DONE")
        } catch (error) {
            console.error('Error downloading document:', error);
        }
    };

    return (
        <div className="app-container">
            <h1>Document Management System</h1>
            <UploadForm onUpload={handleUpload}/>
            <SearchBar onSearch={handleSearch}/>
            <Pagination totalPages={totalPages} currentPage={currentPage} onPageChange={handlePageChange}/>
            <DocumentTable documents={documents} onDelete={handleDelete} onUpdate={handleUpdate}
                           onDownload={handleDownload}/>
        </div>
    );
}

ReactDOM.render(<App/>, document.getElementById('root'));
