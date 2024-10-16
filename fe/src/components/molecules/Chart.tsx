import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { ResponsiveContainer, BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Bar } from 'recharts';
import styled, { useTheme } from 'styled-components';

import { record } from '@/types/walk';

interface ChartProps {
  records: record[];
}

interface ChartType {
  name: string;
  distance: number;
}

const getCurrentWeekRange = (): ChartType[] => {
  const startOfWeek = dayjs().startOf('week'); // 일요일 시작
  return Array.from({ length: 7 }).map((_, index) => ({
    name: startOfWeek.add(index, 'day').format('YYYY-MM-DD'),
    distance: 0,
  }));
};

export default function Chart({ records }: ChartProps) {
  const [chartData, setChartData] = useState<ChartType[]>(getCurrentWeekRange);

  useEffect(() => {
    if (!(records && records.length !== 0)) return;

    // 주간 데이터와 매칭되는 데이터를 병합
    const updatedData = getCurrentWeekRange().map(({ name }) => {
      const recordForDay = records.find((record) => dayjs(record.startDate).format('YYYY-MM-DD') === name);
      return {
        name: name,
        distance: recordForDay ? recordForDay.distance : 0,
      };
    });

    setChartData(updatedData);
  }, [records]);

  const chartColor = useTheme().themeValues.colorValues.special.modalBg;

  return (
    <StyledGraphContainer>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="2 5" />
          <XAxis dataKey="name" />
          <YAxis tickFormatter={(value) => `${value}km`} />
          <Tooltip formatter={(value) => `${value}km`} />
          <Bar dataKey="distance" fill={chartColor} />
        </BarChart>
      </ResponsiveContainer>
    </StyledGraphContainer>
  );
}

const StyledGraphContainer = styled.div`
  width: 100%;
  height: 100%;
  padding-right: 1rem;
  margin: 0 auto;
`;
