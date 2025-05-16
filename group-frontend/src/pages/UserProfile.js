import React from 'react';
import ProfileCard from '../components/common/ProfileCard';
import { useNavigate } from 'react-router-dom';

const UserProfile = () => {
    const navigate = useNavigate();

    const dummyUser = {
        name: 'Sumit Malviya',
        email: 'sumit@example.com',
        phone: '+91 9876543210',
        avatar: 'https://via.placeholder.com/100'
    };

    const handleLogout = () => {
        // Clear auth tokens or user session
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div style={{ padding: '30px' }}>
            <h2 style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <img src="/user.png" alt="User" style={{ width: '24px', height: '24px' }} />
                User Profile
            </h2>

            <ProfileCard {...dummyUser} />
            <div style={{ textAlign: 'center', marginTop: '20px' }}>
                <button onClick={handleLogout} style={{
                    padding: '10px 20px',
                    backgroundColor: '#d9534f',
                    color: 'white',
                    border: 'none',
                    borderRadius: '5px',
                    cursor: 'pointer',
                    fontWeight: 'bold'
                }}>
                    Logout
                </button>
            </div>
        </div>
    );
};

export default UserProfile;
