import React, {useState} from 'react';
import '../style/index.css';

function UploadForm({onUpload}) {
    const [file, setFile] = useState(null);
    const [metadata, setMetadata] = useState({name: '', description: ''});

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleMetadataChange = (e) => {
        const {name, value} = e.target;
        setMetadata((prev) => ({...prev, [name]: value}));
    };

    const handleUpload = () => {
        if (!file) return;
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', metadata.name);
        formData.append('description', metadata.description);
        onUpload(formData);
        setFile(null);
        setMetadata({name: '', description: ''});
    };

    return (
        <div className="upload-form">
            <h2>Upload Document</h2>
            <input type="file" onChange={handleFileChange}/>
            <input type="text" name="name" placeholder="Document Name" value={metadata.name}
                   onChange={handleMetadataChange}/>
            <input type="text" name="description" placeholder="Description" value={metadata.description}
                   onChange={handleMetadataChange}/>
            <button onClick={handleUpload}>Upload</button>
        </div>
    );
}

export default UploadForm;
