import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import authService from '../../services/authService';
import CustomInput from '../common/CustomInput';
import CustomButton from '../common/CustomButton';
import styles from './LoginForm.module.css';

const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await authService.login(username, password);
      navigate('/dashboard');
    } catch (err) {
      alert(err.message || 'Login failed!');
    }
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.card}>
        <h2 className={styles.title}>Login</h2>
        <form onSubmit={handleLogin}>
          <CustomInput
            label="Username"
            type="text"
            placeholder="Enter your username"
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
          {/* <div className={styles.links}> */}
            {/* <Link to="/forgot-password">Forgot Password?</Link> */}
          {/* </div> */}
          <CustomButton text="Login" />
        </form>
        <p className={styles.footer}>
          Don't have an account? <Link to="/register">Register here</Link>
        </p>
      </div>
    </div>
  );
};

export default LoginForm;
