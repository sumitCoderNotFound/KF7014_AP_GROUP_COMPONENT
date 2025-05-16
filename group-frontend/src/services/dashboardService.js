const API_BASE_URL = "http://localhost:8083"; // API Gateway base URL

export const getLatestSafetyFlag = async () => {
  const response = await fetch(`${API_BASE_URL}/api/quality-check/validate`);
  if (!response.ok) throw new Error("Failed to fetch safety flag");
  return response.json();
};

export const getAllWaterQualityRecords = async () => {
  const response = await fetch(`${API_BASE_URL}/api/water-quality/records`);
  if (!response.ok) throw new Error("Failed to fetch water quality records");
  return response.json();
};

