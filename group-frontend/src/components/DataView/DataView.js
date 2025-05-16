import React, { useState } from 'react';
import CustomTable from '../common/CustomTable';

const DataView = () => {
const [loading, setLoading] = useState(false);

  const dummyData = [
    { month: 'April', avgPh: 7.12, avgTDS: 510.45, avgTurbidity: 2.13 },
    { month: 'May', avgPh: 7.08, avgTDS: 499.80, avgTurbidity: 1.96 },
    { month: 'June', avgPh: 7.15, avgTDS: 520.00, avgTurbidity: 2.00 },
    { month: 'Overall Average', avgPh: 7.11, avgTDS: 510.08, avgTurbidity: 2.03 },
  ];

  const columns = [
    { title: 'Month', key: 'month', align: 'left' },
    { title: 'Average pH', key: 'avgPh', align: 'right' },
    { title: 'Average TDS (mg/L)', key: 'avgTDS', align: 'right' },
    { title: 'Average Turbidity (NTU)', key: 'avgTurbidity', align: 'right' },
  ];

  return (
    <div style={{ padding: '20px' }}>
      <h2>📊 Monthly Water Quality Overview</h2>
      <CustomTable
        columns={columns}
        data={dummyData}
        rowsPerPage={10}
        loading={loading}
      />
    </div>
  );
};

export default DataView;
