// src/pages/DashboardPage.js
import React from 'react';
import Dashboard from '../components/Dashboard/Dashboard'; // Adjust the import path as necessary

const DashboardPage = () => {
  console.log("DashboardPage loadeddata"); // ⬅️ Add this too!
  return (
    <div className="container">
      <Dashboard />
    </div>
  );
};

export default DashboardPage;

