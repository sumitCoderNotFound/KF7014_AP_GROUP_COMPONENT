import React, { useEffect, useState } from 'react';
import config from '../../config.js';

const DataView = () => {
  const [monthlyData, setMonthlyData] = useState([]);
  const [loading, setLoading] = useState(true);
  console.log("CONFIG VALUE:", config);
  useEffect(() => {
    const fetchData = async () => {
      try {
        if (config.useMock) {
          console.log("Mock fetch: /tyne/monthly-average");
          setMonthlyData([
            { month: "January", avgPH: 7.2, avgTemp: 8.4 },
            { month: "February", avgPH: 7.1, avgTemp: 9.1 },
            { month: "March", avgPH: 7.3, avgTemp: 10.2 },
          ]);
        } else {
          const res = await fetch('http://localhost:8081/tyne/monthly-average');
          const data = await res.json();
          setMonthlyData(data);
        }
      } catch (err) {
        console.warn("API failed. Showing mock data.");
        setMonthlyData([]);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h2>Monthly Water Quality Overview</h2>
      {loading ? (
        <p>Loading data...</p>
      ) : monthlyData.length === 0 ? (
        <p>No data available.</p>
      ) : (
        <table border="1" cellPadding="10" style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead style={{ backgroundColor: '#f5f5f5' }}>
            <tr>
              <th>Month</th>
              <th>Average pH</th>
              <th>Average Temperature (°C)</th>
            </tr>
          </thead>
          <tbody>
            {monthlyData.map((row, i) => (
              <tr key={i}>
                <td>{row.month}</td>
                <td>{row.avgPH}</td>
                <td>{row.avgTemp}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default DataView;
