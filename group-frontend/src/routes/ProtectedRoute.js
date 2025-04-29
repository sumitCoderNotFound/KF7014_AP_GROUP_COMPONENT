import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  console.log("ProtectedRoute: Token found?", token); // ⬅️ Add this
  return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
