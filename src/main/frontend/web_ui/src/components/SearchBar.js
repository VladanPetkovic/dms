import React from 'react';
import '../style/index.css';

function SearchBar({ onSearch }) {
    const handleSearch = (e) => {
        onSearch(e.target.value);
    };

    return (
        <div className="search-bar">
            <input type="text" placeholder="Search documents..." onChange={handleSearch} />
        </div>
    );
}

export default SearchBar;
