import { useRef, useState } from 'react';

import { createInitTimeRef } from '@/constants/map';
import { StatusOfTime, TimeRef } from '@/types/map';
import { getCurrentDate } from '@/utils/timeUtils';

export const useWalkTime = () => {
  const timeRef = useRef<TimeRef>(createInitTimeRef());

  const [walkStatus, setWalkStatus] = useState<StatusOfTime>('start');

  const startTime = () => {
    timeRef.current.start.day = new Date();
    timeRef.current.start.time = getCurrentDate({ isDay: false, isTime: true });
  };

  return { timeRef, walkStatus, setWalkStatus, startTime };
};
