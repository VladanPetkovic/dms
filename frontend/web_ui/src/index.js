import React, {useState, useEffect} from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import UploadForm from './components/UploadForm';
import SearchBar from './components/SearchBar';
import DocumentTable from './components/DocumentTable';
import {
    getDocuments,
    searchDocuments,
    uploadDocument,
    deleteDocument,
    updateDocumentById,
    downloadDocument
} from './services/documentService';
import Pagination from "./components/Pagination";
import ElasticSearchBar from "./components/ElasticSearchBar";

function App() {
    const [documents, setDocuments] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [elasticQuery, setElasticQuery] = useState('');
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(0);
    const [notification, setNotification] = useState(null);

    useEffect(() => {
        if (elasticQuery && !searchQuery) {
            searchElasticDocuments(); // fetch documents using Elasticsearch
        } else {
            fetchDocuments();
        }
    }, [searchQuery, elasticQuery, currentPage]);

    /**
     * Function to show notification and auto-hide it after 5 seconds
     */
    const showNotification = (message, type = 'success') => {
        setNotification({message, type});
        setTimeout(() => setNotification(null), 5000);
    };

    const fetchDocuments = async () => {
        try {
            const response = await getDocuments(searchQuery, currentPage);
            console.log(response);
            setDocuments(response.content);
            setTotalPages(response.totalPages);
        } catch (error) {
            console.error(error);
            showNotification('Error fetching documents', 'error');
        }
    };

    const searchElasticDocuments = async () => {
        try {
            const response = await searchDocuments(elasticQuery, currentPage);
            console.log(response);
            setDocuments(response.content);
            setTotalPages(response.totalPages);
        } catch (error) {
            console.error(error);
            showNotification('Error searching documents', 'error');
        }
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    };

    const handleDocumentNameSearch = async (query) => {
        setElasticQuery(''); // reset
        setSearchQuery(query);
        setCurrentPage(0);
    };

    const handleElasticSearch = async (query) => {
        setSearchQuery(''); // reset
        setElasticQuery(query);
        setCurrentPage(0);
    };

    const handleUpload = async (formData) => {
        try {
            await uploadDocument(formData);
            await fetchDocuments();
            showNotification('Document uploaded successfully');
        } catch (error) {
            console.error(error);
            showNotification(error.message || 'Failed to upload document', 'error');
        }
    };

    const handleUpdate = async (id, updatedData) => {
        try {
            await updateDocumentById(id, updatedData);
            await fetchDocuments();
            showNotification('Document updated successfully');
        } catch (error) {
            console.error(error);
            showNotification(error.message || 'Failed to update document', 'error');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteDocument(id);
            await fetchDocuments();
            showNotification('Document deleted successfully');
        } catch (error) {
            console.error(error);
            showNotification(error.message || 'Failed to delete document', 'error');
        }
    };

    const handleDownload = async (id) => {
        try {
            await downloadDocument(id);
            console.log("Download initiated");
            showNotification('Document download started!', 'success');
        } catch (error) {
            console.error(error);
            showNotification(error.message || 'Failed to download document', 'error');
        }
    };

    return (
        <div className="app-container">
            <h1>Document Management System</h1>
            {notification && (
                <div className={`notification-banner ${notification.type}`}>
                    {notification.message}
                </div>
            )}
            <UploadForm onUpload={handleUpload}/>
            <SearchBar onSearch={handleDocumentNameSearch}/>
            <ElasticSearchBar onSearch={handleElasticSearch}/>
            <Pagination totalPages={totalPages} currentPage={currentPage} onPageChange={handlePageChange}/>
            <DocumentTable documents={documents} onDelete={handleDelete} onUpdate={handleUpdate}
                           onDownload={handleDownload}/>
        </div>
    );
}

ReactDOM.render(<App/>, document.getElementById('root'));
