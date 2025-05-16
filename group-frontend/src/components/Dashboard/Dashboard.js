import React, { useEffect, useState } from 'react';
import config from '../../config.js';
import { getLatestSafetyFlag, getAllWaterQualityRecords } from '../../services/dashboardService';
import StatusCard from '../common/StatusCard';
import CustomTable from '../common/CustomTable';
import SkeletonCard from '../common/SkeletonCard.js';

const rivers = ['Tyne', 'Wear', 'Mersey', 'Thames'];

const Dashboard = () => {
  const [data, setData] = useState({});
  const [loading, setLoading] = useState(true);
  const [selectedRiver, setSelectedRiver] = useState(null);
  const [records, setRecords] = useState([]);

  useEffect(() => {
    const fetchAll = async () => {
      setLoading(true);
      try {
        const allData = {};
        for (const river of rivers) {
          const result = await getLatestSafetyFlag(); // replace later
          allData[river] = result;
        }
        setData(allData);
        setSelectedRiver('Tyne'); // default selection
        const defaultRecords = await getAllWaterQualityRecords();
        setRecords(defaultRecords);
      } catch (err) {
        console.warn("API error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAll();
  }, []);


  const handleRiverClick = async (river) => {
    setSelectedRiver(river);
    const res = await getAllWaterQualityRecords(); // you can filter later per river
    setRecords(res);
  };

  const tableColumns = [
    { title: 'pH', key: 'pH', align: 'right' },
    { title: 'Alkalinity', key: 'alkalinity', align: 'right' },
    { title: 'TDS (Conductivity)', key: 'conductivity', align: 'right' },
    { title: 'BOD', key: 'bod', align: 'right' },
    { title: 'Nitrite', key: 'nitrite', align: 'right' },
    { title: 'Copper Dissolved 1', key: 'copperDissolved1', align: 'right' },
    { title: 'Copper Dissolved 2', key: 'copperDissolved2', align: 'right' },
    { title: 'Iron Dissolved', key: 'ironDissolved', align: 'right' },
    { title: 'Zinc Dissolved', key: 'zincDissolved', align: 'right' },
    {
      title: 'Timestamp',
      key: 'timestamp',
      align: 'left',
      render: (value) =>
        new Intl.DateTimeFormat('en-GB', {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
        }).format(new Date(value)),
    }

  ];


  return (
    <div style={{ padding: '20px' }}>
      <h2>🌊 Water Quality Dashboard</h2>
      {loading ? (
        <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
          {[...Array(4)].map((_, i) => (
            <SkeletonCard key={i} />
          ))}
        </div>
      ) : (
        <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
          {rivers.map((river) => (
            <div key={river} onClick={() => handleRiverClick(river)} style={{ cursor: 'pointer' }}>
              <StatusCard
                river={river}
                status={data[river]?.safetyFlag || 'Unknown'}
                tds={data[river]?.calculatedTDS || null}
                isActive={selectedRiver === river}
              />

            </div>
          ))}
        </div>
      )}

      {selectedRiver && (
        <div style={{ marginTop: '20px' }}>
          <h3>
            📋 Records for {selectedRiver} River ({records.length} entries)
          </h3>
          <CustomTable
            columns={tableColumns}
            data={records}
            rowsPerPage={10}
            loading={loading}
          />

        </div>
      )}

    </div>
  );
};

export default Dashboard;
