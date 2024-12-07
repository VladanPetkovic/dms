import React from 'react';
import '../style/index.css';

function ElasticSearchBar({onSearch}) {
    const handleSearch = (e) => {
        onSearch(e.target.value);
    };

    return (
        <div className="search-bar">
            <label htmlFor="full-text-search">Search via Document-content:</label>
            <input type="text"
                   id="full-text-search"
                   placeholder="Search..."
                   onChange={handleSearch}/>
        </div>
    );
}

export default ElasticSearchBar;
