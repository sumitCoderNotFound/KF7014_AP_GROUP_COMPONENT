const useMock = true;
const BASE_URL = 'http://localhost:8080';

const mockResponses = {
  '/tyne/latest-flag': { flag: 'Green' },
  '/tyne/monthly-average': [
    { month: 'January', avgPH: 7.2, avgTemp: 8.4 },
    { month: 'February', avgPH: 7.1, avgTemp: 9.1 },
    { month: 'March', avgPH: 7.3, avgTemp: 10.2 },
  ],
};

const apiService = {
  get: async (path) => {
    if (useMock) {
      console.log(`Mock GET ${path}`);
      if (mockResponses[path]) {
        return mockResponses[path];
      } else {
        throw new Error(`No mock data for ${path}`);
      }
    }

    const response = await fetch(`${BASE_URL}${path}`);
    if (!response.ok) throw new Error('Failed to fetch');
    return response.json();
  }
};

export default apiService;
