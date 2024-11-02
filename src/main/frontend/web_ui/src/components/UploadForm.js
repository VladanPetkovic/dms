import React, {useState} from 'react';
import '../style/index.css';

function UploadForm({onUpload}) {
    const [file, setFile] = useState(null);
    const [metadata, setMetadata] = useState({name: '', description: ''});
    const [error, setError] = useState(''); // State to store error messages

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setError(''); // Clear any existing error when a new file is selected
    };

    const handleMetadataChange = (e) => {
        const {name, value} = e.target;
        setMetadata((prev) => ({...prev, [name]: value}));
        setError(''); // Clear any existing error when metadata is changed
    };

    const handleUpload = () => {
        // Validation checks
        if (!file) {
            setError('Please select a file to upload.');
            return;
        }
        if (!metadata.name.trim()) {
            setError('Please enter a document name.');
            return;
        }
        if (!metadata.description.trim()) {
            setError('Please enter a description.');
            return;
        }

        // Clear error if validation passes
        setError('');

        // Prepare form data for upload
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', metadata.name);
        formData.append('description', metadata.description);

        // Trigger the upload function passed as a prop
        onUpload(formData);

        // Reset the form fields
        setFile(null);
        setMetadata({name: '', description: ''});
    };

    return (
        <div className="upload-form">
            <h2>Upload Document</h2>

            {/* Display error message if any validation fails */}
            {error && <div className="error-text">{error}</div>}

            <input type="file" onChange={handleFileChange}/>
            <input
                type="text"
                name="name"
                placeholder="Document Name"
                value={metadata.name}
                onChange={handleMetadataChange}
            />
            <input
                type="text"
                name="description"
                placeholder="Description"
                value={metadata.description}
                onChange={handleMetadataChange}
            />
            <button onClick={handleUpload}>Upload</button>
        </div>
    );
}

export default UploadForm;
