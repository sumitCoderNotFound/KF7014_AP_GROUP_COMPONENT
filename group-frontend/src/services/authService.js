const useMock = true; // set to false when backend is ready

const authService = {
  login: async (email, password) => {
    if (useMock) {
      console.log("Mock login:", email);
      if (email === "test@example.com" && password === "123456") {
        localStorage.setItem('token', 'mock-token-123');
        return { token: 'mock-token-123' };
      } else {
        throw new Error('Invalid credentials');
      }
    }

    const res = await fetch(`http://localhost:8081/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await res.json();
    if (data.token) localStorage.setItem('token', data.token);
    return data;
  },

  register: async (email, password) => {
    if (useMock) {
      console.log("Mock register:", email);
      return { message: "Registered successfully (mock)" };
    }

    const res = await fetch(`http://localhost:8081/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    return res.json();
  },

  changePassword: async (email, newPassword) => {
    if (useMock) {
      console.log("Mock change password for:", email);
      return { message: "Password changed successfully (mock)" };
    }

    const res = await fetch(`http://localhost:8081/change-password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, newPassword }),
    });
    return res.json();
  }
};

export default authService;
