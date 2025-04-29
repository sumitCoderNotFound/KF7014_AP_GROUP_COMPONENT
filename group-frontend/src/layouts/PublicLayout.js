import React from 'react';
import { Outlet } from 'react-router-dom';
import './PublicLayout.css'; // Optional: separate styles for public layout

const PublicLayout = () => {
  return (
    <div className="public-wrapper">
      <Outlet />
    </div>
  );
};

export default PublicLayout;
