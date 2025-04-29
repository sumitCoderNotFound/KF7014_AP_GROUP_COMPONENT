import React, { useState } from 'react';
import CustomInput from '../common/CustomInput';
import CustomButton from '../common/CustomButton';
import './AuthForm.css';

const ForgotPasswordForm = () => {
  const [email, setEmail] = useState('');

  const handleReset = (e) => {
    e.preventDefault();
    alert(`Reset link sent to ${email}`);
  };

  return (
    <div className="wrapper">
      <div className="card">
        <h2 className="title">Forgot Password</h2>
        <form onSubmit={handleReset}>
          <CustomInput
            label="Email"
            type="email"
            placeholder="Enter your registered email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <CustomButton text="Send Reset Link" />
        </form>
      </div>
    </div>
  );
};

export default ForgotPasswordForm;
