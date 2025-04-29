import React from 'react';

const CustomInput = ({ label, type = 'text', value, onChange, placeholder }) => (
  <div style={{ marginBottom: '16px' }}>
    <label style={{ display: 'block', marginBottom: '6px', fontWeight: 'bold' }}>{label}</label>
    <input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      style={{
        width: '100%',
        padding: '10px',
        borderRadius: '5px',
        border: '1px solid #ccc',
      }}
    />
  </div>
);

export default CustomInput;
