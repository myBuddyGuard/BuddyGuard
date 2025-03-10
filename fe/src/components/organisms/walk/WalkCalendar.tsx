import dayjs from 'dayjs';
import React, { useEffect, useMemo, useState } from 'react';
import { Calendar } from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import styled from 'styled-components';

import { useWalkQuery } from '@/hooks/walk/useWalkQuery';
// import { testData } from '@/mocks/walkTest';
import { useFilterStore } from '@/stores/useFilterStore';
import { usePetStore } from '@/stores/usePetStore';
import Stamp from '@/svg/walk_stamp.svg';
import { FilterType, record } from '@/types/walk';

type ValuePiece = Date | null;
type Value = ValuePiece | [ValuePiece, ValuePiece];

interface WalkCalendarProps {
  setSelectedData: React.Dispatch<React.SetStateAction<record | null>>;
}
export default function WalkCalendar({ setSelectedData }: WalkCalendarProps) {
  const { setAll } = useFilterStore();
  const { selectedBuddy } = usePetStore();

  const [activeDate, setActiveDate] = useState<Date | null>(null);
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);

  useEffect(() => {
    setActiveDate(new Date());
    setSelectedDate(new Date());
  }, []);

  const queryParams = useMemo(
    () => ({
      filterKey: 'all' as FilterType,
      buddyId: selectedBuddy?.petId ?? 0,
      month: (activeDate?.getMonth() ?? new Date().getMonth()) + 1,
      year: activeDate?.getFullYear() ?? new Date().getFullYear(),
    }),
    [activeDate]
  );

  const { data, isLoading, refetch } = useWalkQuery(queryParams);
  // const { isLoading, refetch, error } = useWalkQuery(queryParams);
  // const data = testData;

  const handleDateChange = (newDate: Value) => {
    if (!(newDate instanceof Date)) return;
    if (isLoading) return;

    setSelectedDate(newDate);
    setActiveDate(newDate);

    const selectedRecord = data.records?.find(({ startDate }: { startDate: string }) =>
      dayjs(startDate).isSame(newDate, 'day')
    );

    if (selectedRecord) setSelectedData(selectedRecord);
    else setSelectedData(null);
  };

  const handleMonthChange = ({ activeStartDate }: { activeStartDate: Date | null }) => {
    if (!activeStartDate) return;
    setSelectedDate(activeStartDate);
    setActiveDate(activeStartDate);
  };

  // 캘린더에 일정이 있으면 점 표시
  const tileContent = useMemo(() => {
    return ({ date, view }: { date: Date; view: string }) => {
      const formattedDate = dayjs(date).format('YYYY-MM-DD');
      const schedule = data?.records?.find(({ startDate }: { startDate: string }) => startDate === formattedDate);
      return schedule && view === 'month' ? <StyledStamp /> : null;
    };
  }, [data?.records]);

  useEffect(() => {
    const newMonth = (activeDate?.getMonth() ?? new Date().getMonth()) + 1;
    const newYear = activeDate?.getFullYear() ?? new Date().getFullYear();
    setAll(newMonth, newYear);
    // refetch();
  }, [activeDate]);

  return (
    <StyledCalendarWrapper>
      {!isLoading && activeDate && (
        <StyledCalendar
          value={selectedDate} // 현재 선택된 날짜
          onChange={handleDateChange} // 날짜 선택 시 호출되는 함수
          onActiveStartDateChange={handleMonthChange} // 달력의 네비게이션이 변경될 때 호출되는 함수
          tileContent={tileContent}
          calendarType="gregory"
          showNeighboringMonth={false}
          next2Label={null}
          prev2Label={null}
          minDetail="year"
          locale="ko"
        />
      )}
    </StyledCalendarWrapper>
  );
}

// 스타일
const StyledCalendarWrapper = styled.div`
  padding: 1rem;
  width: 100%;
  display: flex;
  justify-content: center;
  position: relative;

  & .react-calendar {
    width: 100%;
    border: none;
    border-radius: 0.5rem;
    box-shadow: ${({ theme }) => `0 0 0.81rem 0.125rem ${theme.currentTheme.shadow}`};
    background-color: transparent;
    padding: 0.5rem;
  }

  & abbr {
    color: ${({ theme }) => theme.currentTheme.textPrimary};
  }

  & .react-calendar__navigation {
    margin-bottom: 0rem;
    height: 2rem;

    // @media (min-width: 60rem) {
    //   margin-bottom: 1rem;
    //   height: 3rem;
    // }
  }

  & .react-calendar__navigation * {
    background-color: ${({ theme }) => theme.currentTheme.widgetBlue};
    color: ${({ theme }) => theme.currentTheme.textSecondary};
  }

  & .react-calendar__navigation__prev-button {
    border-radius: 0.5rem 0 0 0.5rem;
  }
  & .react-calendar__navigation__next-button {
    border-radius: 0 0.5rem 0.5rem 0;
  }

  & .react-calendar__tile {
    position: relative;
    display: flex;
    justify-content: center;
    height: 2rem;
    // @media (min-width: 60rem) {
    //   height: 4rem;
    // }

    & abbr {
      position: absolute;
      top: 0.5rem;
      // @media (min-width: 60rem) {
      //   top: 1.5rem;
      // }

      z-index: 999;
    }
  }

  & .react-calendar__tile--now {
    background-color: gray;
    opacity: 50%;
    border-radius: 0.3rem;
    abbr {
      color: white;
    }
  }

  & .react-calendar__tile--active {
    border-radius: 0.2rem;
    abbr {
      color: white;
    }
  }
`;

const StyledCalendar = styled(Calendar)`
  height: 20%;
`;

const StyledStamp = styled(Stamp)`
  opacity: 70%;
  position: absolute;

  top: 0rem;
  width: 2rem;
  height: 2rem;

  // @media (min-width: 60rem) {
  //   top: 0.4rem;
  //   width: 3rem;
  //   height: 3rem;
  // }
`;
