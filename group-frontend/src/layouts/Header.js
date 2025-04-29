import React from 'react';
import { Link } from 'react-router-dom';


const Header = () => (
  <nav>
    <Link to="/dashboard">Dashboard</Link> | 
    <Link to="/data">Data View</Link> | 
    <Link to="/change-password">Change Password</Link> | 
    <Link to="/login">Login</Link> | 
    <Link to="/register">Register</Link>
  </nav>
);

export default Header;
