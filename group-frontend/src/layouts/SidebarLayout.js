import React from 'react';
import { NavLink, useNavigate, Outlet } from 'react-router-dom';
import './SidebarLayout.css';

const SidebarLayout = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="layout-container">
            <aside className="sidebar">
                <h2 className="logo">WQ Monitor</h2>
                <nav>
                    <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                        Dashboard
                    </NavLink>
                    <NavLink to="/data" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                        Data View
                    </NavLink>
                </nav>

            </aside>

            <main className="main-content">
                <header className="topbar">
                    <div className="profile-info">
                        <span>👤 User</span>
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                </header>
                <div className="content-body">
                    <Outlet /> {/* ✅ This is the correct way */}
                </div>
            </main>
        </div>
    );
};

export default SidebarLayout;
