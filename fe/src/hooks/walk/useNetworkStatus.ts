import { message } from 'antd';
import { useCallback, useEffect, useState } from 'react';

import { OFFLINE_PATH_STORAGE_KEY } from '@/constants/map';

export const useNetworkStatus = () => {
  const [isOnline, setIsOnline] = useState<boolean>(navigator.onLine);
  const [hasOfflineData, setHasOfflineData] = useState<boolean>(false);

  const loadOfflinePathData = () => {
    try {
      const saved = localStorage.getItem(OFFLINE_PATH_STORAGE_KEY);
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      console.error('임시 데이터 로드 실패:', error);
      return null;
    }
  };

  /** 저장된 데이터 확인 */
  const checkOfflineData = useCallback(() => {
    const savedPathData = loadOfflinePathData();
    if (savedPathData) {
      setHasOfflineData(true);
    } else {
      setHasOfflineData(false);
    }
  }, []);

  // 컴포넌트 마운트 시 저장된 데이터 확인
  useEffect(() => {
    checkOfflineData();
  }, [checkOfflineData]);

  useEffect(() => {
    const handleOnline = () => {
      setIsOnline(true);
      checkOfflineData();
      message.info('네트워크가 재연결 되었습니다.');
    };

    const handleOffline = () => {
      setIsOnline(false);
      message.warning('네트워크가 끊겼습니다.');
    };

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, [checkOfflineData]);

  return { isOnline };
};
