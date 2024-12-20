import React from 'react';
import '../style/index.css';

function SearchBar({onSearch}) {
    const handleSearch = (e) => {
        onSearch(e.target.value);
    };

    return (
        <div className="search-bar">
            <label htmlFor="name-search">Search via Document-name:</label>
            <input type="text"
                   id="name-search"
                   placeholder="Search..."
                   onChange={handleSearch}/>
        </div>
    );
}

export default SearchBar;
