// src/components/common/SkeletonCard.js
import React from 'react';

const SkeletonCard = () => {
  return (
    <div style={{
      border: '1px solid #ddd',
      borderRadius: '10px',
      padding: '20px',
      width: '250px',
      backgroundColor: '#f3f3f3',
      boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      marginBottom: '20px',
      animation: 'pulse 1.5s infinite',
    }}>
      <div style={{ height: '20px', width: '60%', backgroundColor: '#ddd', borderRadius: '4px', marginBottom: '10px' }} />
      <div style={{ height: '16px', width: '80%', backgroundColor: '#ddd', borderRadius: '4px', marginBottom: '8px' }} />
      <div style={{ height: '16px', width: '70%', backgroundColor: '#ddd', borderRadius: '4px' }} />
    </div>
  );
};

export default SkeletonCard;
