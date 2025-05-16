import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import './SidebarLayout.css';

const SidebarLayout = () => {


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
                        <NavLink to="/profile" className="nav-link user-icon" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                            <img src="/user.png" alt="User" style={{ width: '30px', height: '30px', borderRadius: '50%' }} />
                            <span>User</span>
                        </NavLink>

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
