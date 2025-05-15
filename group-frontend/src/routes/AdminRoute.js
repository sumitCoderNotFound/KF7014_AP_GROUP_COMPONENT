// src/routes/AdminRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';

const AdminRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('role'); // assuming you store user role

  if (!token) return <Navigate to="/login" />;
  if (userRole !== 'admin') return <Navigate to="/dashboard" />;

  return children;
};

export default AdminRoute;
