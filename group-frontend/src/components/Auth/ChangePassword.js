import React, { useState } from 'react';
import authService from '../../services/authService';
import '../../App.css';

const ChangePassword = () => {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');

  const handleChangePassword = async (e) => {
    e.preventDefault();
    try {
      await authService.changePassword(oldPassword, newPassword);
      alert('Password changed successfully');
    } catch (err) {
      alert(err.message || 'Failed to change password!');
    }
  };

  return (
    <div className="container">
      <h2>Change Password</h2>
      <form onSubmit={handleChangePassword}>
        <input
          type="password"
          placeholder="Current Password"
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="New Password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <button type="submit">Change Password</button>
      </form>
    </div>
  );
};

export default ChangePassword;
