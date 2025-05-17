import React, { useState } from 'react';
import CustomInput from '../common/CustomInput';
import CustomButton from '../common/CustomButton';
import { Link } from 'react-router-dom';
import './AuthForm.css';

const RegisterForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleRegister = (e) => {
    e.preventDefault();
    alert(`Registered with email: ${email}`);
  };

  return (
    <div className="wrapper">
      <div className="card">
        <h2 className="title">Register</h2>
        <form onSubmit={handleRegister}>
          <CustomInput
            label="Email"
            type="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
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
