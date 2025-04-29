import React, { useEffect, useState } from 'react';
import config from '../../config.js'; // import global config

const Dashboard = () => {
  const [flag, setFlag] = useState(null);
  const [loading, setLoading] = useState(true);
  console.log("Dashboard rendered");
  console.log("Mock mode?", config);
  
  useEffect(() => {
    const fetchFlag = async () => {
      try {
        if (config.useMock) {
          console.log("Mocking flag data");
          setFlag("Green");
        } else {
          const res = await fetch('http://localhost:8081/tyne/latest-flag');
          const data = await res.json();
          setFlag(data.flag);
        }
      } catch (err) {
        console.warn("API error:", err);
        setFlag("Green");
      } finally {
        setLoading(false);
      }
    };

    fetchFlag();
    const interval = setInterval(fetchFlag, 30000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h2>Tyne River Safety Flag</h2>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <p>
          Status:{" "}
          <strong style={{ color: flag === 'Red' ? 'red' : 'green' }}>
            {flag}
          </strong>
        </p>
      )}
    </div>
  );
};

export default Dashboard;
