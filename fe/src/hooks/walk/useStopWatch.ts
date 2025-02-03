import { useCallback, useEffect, useRef, useState } from 'react';

import { getTimeFormatString } from '@/helper/timerHelpers';
import { StatusOfTime, TimeRef } from '@/types/map';

export default function useStopWatch(status: StatusOfTime, timeRef: React.MutableRefObject<TimeRef>) {
  const [time, setTime] = useState(0);
  const timeoutRef = useRef<number | null>(null);

  const startTimer = useCallback(() => {
    timeoutRef.current = window.setTimeout(() => {
      setTime((prev) => prev + 1);
      startTimer();
    }, 1000);
  }, []);

  const stopTimer = useCallback(() => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
      timeoutRef.current = null;
    }
  }, []);

  useEffect(() => {
    if (status === 'start' && !timeoutRef.current) {
      startTimer();
      return;
    }

    if (status === 'pause') {
      stopTimer();

      return;
    }

    if (status === 'stop') {
      timeRef.current.total = getTimeFormatString(time);
      stopTimer();
      // setTime(0);
    }

    return () => stopTimer();
  }, [status, startTimer, stopTimer, time, timeRef]);

  return time;
}
