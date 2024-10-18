import moment from 'moment';
// import React, { useState } from 'react';
import React from 'react';
import { Calendar } from 'react-calendar';

import styled from 'styled-components';

// 카테고리별 색상 맵
const categoryColors: { [key: string]: string } = {
  건강: '#ff9999',
  산책: '#99ccff',
  식사: '#ffcc99',
  체중: '#A6C8DD',
};

interface Schedule {
  id: number;
  date: string;
  category: string;
  title: string;
  time: string;
  description: string;
}

interface CommonCalendarProps {
  schedules?: Schedule[];
  onDateChange: (date: string) => void;
}

// export const CommonCalendar: React.FC<CommonCalendarProps> = ({ schedules = [], onDateChange }) => {
export const CommonCalendar: React.FC<CommonCalendarProps> = ({ schedules = [] }) => {
  // const [date, setDate] = useState<Date | null>(null);

  // const handleDateChange = (newDate: Date) => {
  //   const formattedDate = moment(newDate).format('YYYY-MM-DD');
  //   setDate(newDate);
  //   onDateChange(formattedDate);
  // };

  // 캘린더에 일정이 있으면 점 표시
  const tileContent = ({ date, view }: { date: Date; view: string }) => {
    const formattedDate = moment(date).format('YYYY-MM-DD');
    const schedule = schedules?.find((schedule) => schedule.date === formattedDate);
    return schedule && view === 'month' ? <Dot color={categoryColors[schedule.category]} /> : null;
  };

  return (
    <StyledCalendarWrapper>
      <StyledCalendar
        // value={date}
        // onChange={handleDateChange}
        tileContent={tileContent}
        formatDay={(date) => moment(date).format('D')}
        formatYear={(date) => moment(date).format('YYYY')}
        formatMonthYear={(date) => moment(date).format('YYYY. MM')}
        calendarType="gregory"
        showNeighboringMonth={false}
        next2Label={null}
        prev2Label={null}
        minDetail="year"
        locale="ko"
      />
    </StyledCalendarWrapper>
  );
};

// 스타일
const StyledCalendarWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  position: relative;

  .react-calendar {
    width: 100%;
    border: none;
    border-radius: 0.5rem;
    box-shadow: 0 0 5px 5px rgba(0, 0, 0, 0.13);
    padding: 3% 5%;
    background-color: white;
  }

  .react-calendar__tile--now {
    background-color: gray;
    opacity: 50%;
    border-radius: 0.3rem;
    abbr {
      color: white;
    }
  }

  .react-calendar__tile--active {
    border-radius: 0.2rem;
    abbr {
      color: white;
    }
  }
`;

const StyledCalendar = styled(Calendar)``;

// 일정에 점 표시 스타일
const Dot = styled.div<{ color: string }>`
  width: 8px;
  height: 8px;
  background-color: ${({ color }) => color};
  border-radius: 50%;
  margin: 0 auto;
`;

export default CommonCalendar;
