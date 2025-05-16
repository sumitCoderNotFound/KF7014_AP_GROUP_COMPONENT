import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import LoginPage from '../pages/Login';
import RegisterPage from '../pages/Register';
import ForgotPasswordPage from '../pages/ForgotPassword';
import DashboardPage from '../pages/DashboardPage';
import DataViewPage from '../pages/DataView';

import ProtectedRoute from './ProtectedRoute';
import AdminRoute from './AdminRoute'; // NEW
import PublicLayout from '../layouts/PublicLayout';
import SidebarLayout from '../layouts/SidebarLayout';
import AdminDataPage from '../pages/AdminDataPage';
import UserProfile from '../pages/UserProfile';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Redirect root to login */}
      <Route path="/" element={<Navigate to="/login" />} />

      {/* Public Routes with Public Layout */}
      <Route element={<PublicLayout />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      </Route>

      {/* Protected Routes with Sidebar Layout */}
      <Route element={<SidebarLayout />}>
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/data"
          element={
            <ProtectedRoute>
              <DataViewPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <UserProfile />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/data"
          element={
            <AdminRoute>
              <AdminDataPage />
            </AdminRoute>
          }
        />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
