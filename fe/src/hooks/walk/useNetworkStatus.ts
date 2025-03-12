import { message } from 'antd';
import { useCallback, useEffect, useState } from 'react';

import { FormDataType } from '@/components/organisms/walk/WalkModal';
import { OFFLINE_PATH_STORAGE_KEY } from '@/constants/map';

// 저장할 데이터 타입 정의
export interface OfflineWalkData {
  path: FormDataType['path'];
  time: {
    start: { date: string; time: string };
    end: { date: string; time: string };
    totalTime: string;
  };
  selectedBuddyIds: FormDataType['buddysId'];
  note?: FormDataType['note'];
  savedAt: number; // 타임스탬프
}

export const useNetworkStatus = () => {
  const [isOnline, setIsOnline] = useState<boolean>(navigator.onLine);
  const [hasOfflineData, setHasOfflineData] = useState<boolean>(false);

  /** 경로 데이터 저장 */
  const savePathData = useCallback((data: OfflineWalkData) => {
    try {
      localStorage.setItem(
        OFFLINE_PATH_STORAGE_KEY,
        JSON.stringify({
          ...data,
          savedAt: new Date().getTime(), // 저장 시간 기록
        })
      );
      setHasOfflineData(true);
      return true;
    } catch (error) {
      console.error('임시 데이터 저장 실패:', error);
      return false;
    }
  }, []);

  /** 저장된 오프라인 데이터 불러오기 */
  const loadOfflinePathData = useCallback(() => {
    try {
      const saved = localStorage.getItem(OFFLINE_PATH_STORAGE_KEY);
      return saved ? JSON.parse(saved) : null;
    } catch (error) {
      console.error('임시 데이터 로드 실패:', error);
      return null;
    }
  }, []);

  /** 저장된 데이터 확인 */
  const checkOfflineData = useCallback(() => {
    const savedPathData = loadOfflinePathData();

    if (savedPathData) {
      setHasOfflineData(true);
    } else {
      setHasOfflineData(false);
    }
  }, [loadOfflinePathData]);

  /** 오프라인 데이터 삭제 */
  const discardOfflineData = useCallback(() => {
    localStorage.removeItem(OFFLINE_PATH_STORAGE_KEY);
    setHasOfflineData(false);
  }, []);

  /** 오프라인 데이터 복구 */
  const recoverOfflinePathData = useCallback(() => {
    const savedData = loadOfflinePathData();
    if (savedData) discardOfflineData(); // 복구 후 삭제

    return savedData;
  }, [loadOfflinePathData, discardOfflineData]);

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

  return { isOnline, hasOfflineData, savePathData, recoverOfflinePathData, loadOfflinePathData };
};
