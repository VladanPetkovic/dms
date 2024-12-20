import React from 'react';
import PropTypes from 'prop-types';

const Pagination = ({ totalPages, currentPage, onPageChange }) => {
    const handlePrevious = () => {
        if (currentPage > 0) {
            onPageChange(currentPage - 1);
        }
    };

    const handleNext = () => {
        if (currentPage < totalPages - 1) {
            onPageChange(currentPage + 1);
        }
    };

    return (
        <div className="pagination">
            <button onClick={handlePrevious} disabled={currentPage === 0}>
                Previous
            </button>
            <span>
                Page {currentPage + 1} of {totalPages}
            </span>
            <button onClick={handleNext} disabled={currentPage === totalPages - 1}>
                Next
            </button>
        </div>
    );
};

Pagination.propTypes = {
    totalPages: PropTypes.number.isRequired,
    currentPage: PropTypes.number.isRequired,
    onPageChange: PropTypes.func.isRequired,
};

export default Pagination;
