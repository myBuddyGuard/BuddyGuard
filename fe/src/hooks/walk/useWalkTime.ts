import { useRef, useState } from 'react';

import { StatusOfTime, TimeRef } from '@/types/map';
import { getCurrentDate } from '@/utils/timeUtils';

export const useWalkTime = () => {
  const initTimeRef: TimeRef = {
    start: { day: new Date(), time: '' },
    end: { day: new Date(), time: '' },
    total: '',
  };

  const timeRef = useRef<TimeRef>(initTimeRef);

  const [walkStatus, setWalkStatus] = useState<StatusOfTime>('start');

  const startTime = () => {
    timeRef.current.start.day = new Date();
    timeRef.current.start.time = getCurrentDate({ isDay: false, isTime: true });
  };

  return { timeRef, walkStatus, setWalkStatus, startTime };
};
