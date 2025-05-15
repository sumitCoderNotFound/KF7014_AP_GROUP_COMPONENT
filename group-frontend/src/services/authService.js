const API_BASE_URL = 'http://localhost:8090/api/authenticate';

const authService = {
  login: async (username, password) => {
    try {
      const res = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (!res.ok) throw new Error(data.message || 'Login failed');

      if (data.token) localStorage.setItem('token', data.token);

      return data;
    } catch (err) {
      console.error('Login Error:', err);
      throw err;
    }
  },

  register: async (username, password) => {
    try {
      const res = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.text();

      if (!res.ok) throw new Error(data || 'Registration failed');

      return { message: data };
    } catch (err) {
      console.error('Registration Error:', err);
      throw err;
    }
  },

  changePassword: async (oldPassword, newPassword) => {
    try {
      const token = localStorage.getItem('token');
      if (!token) throw new Error('No token found. Please login.');

      const res = await fetch(`${API_BASE_URL}/changepassword`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ oldPassword, newPassword }),
      });

      const data = await res.text();

      if (!res.ok) throw new Error(data || 'Password change failed');

      return { message: data };
    } catch (err) {
      console.error('Change Password Error:', err);
      throw err;
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    return { message: 'Logged out successfully.' };
  },
};

export default authService;
