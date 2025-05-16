import React from 'react';

 
const ProfileCard = ({ avatar, name, email, phone }) => {
  return (
    <div style={{
      border: '1px solid #ddd',
      borderRadius: '10px',
      padding: '20px',
      maxWidth: '400px',
      margin: '0 auto',
      backgroundColor: '#fff',
      boxShadow: '0 2px 10px rgba(0,0,0,0.08)'
    }}>
      <img
        src="/user.png"
        alt="Profile"
        style={{
          width: '100px',
          height: '100px',
          borderRadius: '50%',
          objectFit: 'cover',
          marginBottom: '15px'
        }}
      />
      <h2 style={{ margin: '10px 0' }}>{name}</h2>
      <p><strong>Email:</strong> {email}</p>
      <p><strong>Mobile:</strong> {phone}</p>
    </div>
  );
};

export default ProfileCard;
