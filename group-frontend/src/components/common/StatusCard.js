import React from 'react';

const StatusCard = ({ river, status, tds, isActive }) => {
  return (
    <div style={{
      border: isActive ? '2px solid #2196f3' : '1px solid #ddd',
      borderRadius: '10px',
      padding: '20px',
      width: '250px',
      backgroundColor: '#fff',
      boxShadow: isActive ? '0 4px 12px rgba(33, 150, 243, 0.2)' : '0 2px 8px rgba(0,0,0,0.1)',
      marginBottom: '20px',
      transition: 'all 0.3s ease-in-out',
    }}>
      <h3 style={{ marginBottom: '10px' }}>{river} River</h3>
      <p>
        <strong>Status:</strong>{' '}
        <span style={{ color: status === 'Red' ? 'red' : 'green', fontWeight: 'bold' }}>{status}</span>
      </p>
      <p>
        <strong>Calculated TDS:</strong> {tds !== null ? `${tds.toFixed(2)} mg/L` : 'N/A'}
      </p>
    </div>
  );
};

export default StatusCard;
