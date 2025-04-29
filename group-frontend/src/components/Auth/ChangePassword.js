import React, { useState } from 'react';
import authService from '../../services/authService';
import '../../App.css'; // Assuming you have some CSS for styling

const ChangePassword = () => {
  const [email, setEmail] = useState('');
  const [newPassword, setNewPassword] = useState('');

  const handleChangePassword = async (e) => {
    e.preventDefault();
    try {
      await authService.changePassword(email, newPassword);
      alert('Password changed!');
    } catch {
      alert('Failed to change password!');
    }
  };

  return (
    <div className="container">
      <h2>Change Password</h2>
      <form onSubmit={handleChangePassword}>
        <input placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" placeholder="New Password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
        <button type="submit">Change Password</button>
      </form>
    </div>
  );
};

export default ChangePassword;
