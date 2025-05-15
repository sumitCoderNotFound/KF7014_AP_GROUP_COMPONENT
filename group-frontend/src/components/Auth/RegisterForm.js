import React, { useState } from 'react';
import authService from '../../services/authService';
import CustomInput from '../common/CustomInput';
import CustomButton from '../common/CustomButton';
import { Link, useNavigate } from 'react-router-dom';
import './AuthForm.css';

const RegisterForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const res = await authService.register(username, password);
      alert(res.message);
      navigate('/login');
    } catch (err) {
      alert(err.message || 'Registration failed');
    }
  };

  return (
    <div className="wrapper">
      <div className="card">
        <h2 className="title">Register</h2>
        <form onSubmit={handleRegister}>
          <CustomInput
            label="Username"
            type="text"
            placeholder="Enter username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <CustomInput
            label="Password"
            type="password"
            placeholder="Enter your password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <CustomButton text="Register" />
        </form>
        <p className="footer">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default RegisterForm;
