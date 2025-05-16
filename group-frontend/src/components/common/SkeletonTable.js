// src/components/common/SkeletonTable.js
import React from 'react';

const SkeletonTable = ({ rows = 10, columns = 8 }) => {
  const rowArray = Array.from({ length: rows });
  const colArray = Array.from({ length: columns });

  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '15px' }}>
        <thead>
          <tr>
            {colArray.map((_, colIndex) => (
              <th key={colIndex} style={{ padding: '12px', backgroundColor: '#f0f0f0' }}>
                <div
                  style={{
                    height: '14px',
                    width: '80%',
                    backgroundColor: '#ddd',
                    borderRadius: '4px',
                    margin: '0 auto',
                    animation: 'pulse 1.5s infinite',
                  }}
                />
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rowArray.map((_, rowIndex) => (
            <tr key={rowIndex}>
              {colArray.map((_, colIndex) => (
                <td key={colIndex} style={{ padding: '12px' }}>
                  <div
                    style={{
                      height: '14px',
                      width: '100%',
                      backgroundColor: '#e0e0e0',
                      borderRadius: '4px',
                      animation: 'pulse 1.5s infinite',
                    }}
                  />
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default SkeletonTable;
