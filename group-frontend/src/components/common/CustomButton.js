import React from 'react';

const CustomButton = ({ text, onClick, type = 'submit' }) => (
  <button
    type={type}
    onClick={onClick}
    style={{
      padding: '10px 20px',
      backgroundColor: '#004477',
      color: 'white',
      border: 'none',
      borderRadius: '4px',
      fontWeight: 'bold',
      cursor: 'pointer'
    }}
  >
    {text}
  </button>
);

export default CustomButton;
