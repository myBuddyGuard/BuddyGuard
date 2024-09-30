import { useCallback, useEffect, useRef, useState } from 'react';

import { StatusOfTime } from '@/types/map';

export default function useStopWatch(status: StatusOfTime) {
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
    }
  }, []);

  useEffect(() => {
    if (status === 'start' && !timeoutRef.current) {
      startTimer();
      return;
    }

    if (status === 'pause') {
      stopTimer();
      timeoutRef.current = null;
      return;
    }

    if (status === 'stop') {
      stopTimer();
      setTime(0);
      timeoutRef.current = null;
    }

    return () => stopTimer();
  }, [status, startTimer, stopTimer]);

  return time;
}