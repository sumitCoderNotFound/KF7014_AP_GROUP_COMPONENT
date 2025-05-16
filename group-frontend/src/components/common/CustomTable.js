import React, { useState } from 'react';

const CustomTable = ({ columns, data, rowsPerPage = 10, loading = false }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [sortKey, setSortKey] = useState('');
  const [sortOrder, setSortOrder] = useState('asc');
  const [currentPage, setCurrentPage] = useState(1);

  const handleSort = (key) => {
    if (sortKey === key) {
      setSortOrder((prev) => (prev === 'asc' ? 'desc' : 'asc'));
    } else {
      setSortKey(key);
      setSortOrder('asc');
    }
  };

  const filteredData = data
    .filter((item) =>
      Object.values(item).some((val) =>
        String(val).toLowerCase().includes(searchTerm.toLowerCase())
      )
    )
    .sort((a, b) => {
      if (!sortKey) return 0;
      const aVal = a[sortKey];
      const bVal = b[sortKey];
      if (typeof aVal === 'number' && typeof bVal === 'number') {
        return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
      }
      return sortOrder === 'asc'
        ? String(aVal).localeCompare(String(bVal))
        : String(bVal).localeCompare(String(aVal));
    });

  const totalPages = Math.ceil(filteredData.length / rowsPerPage);
  const paginatedData = filteredData.slice(
    (currentPage - 1) * rowsPerPage,
    currentPage * rowsPerPage
  );

  const goToPage = (pageNum) => {
    if (pageNum >= 1 && pageNum <= totalPages) setCurrentPage(pageNum);
  };

  return (
    <div>
      {/* Search */}
      <input
        placeholder="Search..."
        value={searchTerm}
        onChange={(e) => {
          setSearchTerm(e.target.value);
          setCurrentPage(1);
        }}
        style={{
          marginBottom: '10px',
          padding: '6px 10px',
          width: '250px',
          borderRadius: '4px',
          border: '1px solid #ccc',
          fontSize: '14px',
        }}
        disabled={loading}
      />

      {/* Table */}
      <div
        style={{
          maxHeight: '400px',
          overflowY: 'auto',
          overflowX: 'auto',
          border: '1px solid #e0e0e0',
          borderRadius: '8px',
        }}
      >
        <table style={{ minWidth: '1000px', width: '100%', borderCollapse: 'collapse', fontSize: '15px' }}>
          <thead style={{ background: '#f5f5f5', position: 'sticky', top: 0, zIndex: 1 }}>
            <tr>
              {columns.map((col) => (
                <th
                  key={col.key}
                  onClick={() => handleSort(col.key)}
                  style={{
                    cursor: loading ? 'default' : 'pointer',
                    padding: '10px 8px',
                    borderBottom: '2px solid #ddd',
                    textAlign: col.align || 'left',
                    background: '#f5f5f5',
                  }}
                >
                  {col.title} {sortKey === col.key && !loading ? (sortOrder === 'asc' ? '↑' : '↓') : ''}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {loading
              ? Array.from({ length: rowsPerPage }).map((_, i) => (
                  <tr key={`skeleton-${i}`}>
                    {columns.map((col, j) => (
                      <td
                        key={`skeleton-cell-${j}`}
                        style={{
                          padding: '10px 8px',
                          textAlign: col.align || 'right',
                        }}
                      >
                        <div
                          style={{
                            height: '14px',
                            backgroundColor: '#eee',
                            borderRadius: '4px',
                            width: `${40 + Math.random() * 40}%`,
                            animation: 'pulse 1.5s infinite',
                          }}
                        />
                      </td>
                    ))}
                  </tr>
                ))
              : paginatedData.length === 0 ? (
                <tr>
                  <td colSpan={columns.length} style={{ padding: '10px', textAlign: 'center' }}>
                    No matching data found
                  </td>
                </tr>
              ) : (
                paginatedData.map((row, i) => (
                  <tr
                    key={i}
                    style={{
                      borderBottom: '1px solid #eee',
                      backgroundColor: i % 2 === 0 ? '#ffffff' : '#fafafa',
                      transition: 'background 0.3s',
                    }}
                    onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = '#f1f1f1')}
                    onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = i % 2 === 0 ? '#ffffff' : '#fafafa')}
                  >
                    {columns.map((col) => (
                      <td
                        key={col.key}
                        style={{
                          padding: '10px 8px',
                          textAlign: col.align || 'right',
                          whiteSpace: 'nowrap',
                        }}
                      >
                        {col.render ? col.render(row[col.key], row) : row[col.key]}
                      </td>
                    ))}
                  </tr>
                ))
              )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {!loading && totalPages > 1 && (
        <div style={{ marginTop: '15px', textAlign: 'center' }}>
          <button
            onClick={() => goToPage(currentPage - 1)}
            disabled={currentPage === 1}
            style={paginationButtonStyle}
          >
            «
          </button>

          {Array.from({ length: totalPages }, (_, i) => i + 1)
            .filter((pageNum) => Math.abs(pageNum - currentPage) <= 1)
            .map((pageNum) => (
              <button
                key={pageNum}
                onClick={() => goToPage(pageNum)}
                style={{
                  ...paginationButtonStyle,
                  backgroundColor: currentPage === pageNum ? '#2196f3' : '#fff',
                  color: currentPage === pageNum ? '#fff' : '#000',
                }}
              >
                {pageNum}
              </button>
            ))}

          <button
            onClick={() => goToPage(currentPage + 1)}
            disabled={currentPage === totalPages}
            style={paginationButtonStyle}
          >
            »
          </button>
        </div>
      )}
    </div>
  );
};

const paginationButtonStyle = {
  margin: '0 3px',
  padding: '6px 12px',
  border: '1px solid #ccc',
  borderRadius: '4px',
  cursor: 'pointer',
  fontWeight: 'bold',
  minWidth: '36px',
};


export default CustomTable;
