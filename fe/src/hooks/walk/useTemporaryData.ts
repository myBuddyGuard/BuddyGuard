import { useCallback, useEffect, useState } from 'react';

import { OFFLINE_PATH_STORAGE_KEY } from '@/constants/map';

import { OfflinePathData } from './useNetworkStatus';

export const useTemporaryData = (isOnline: boolean) => {
  const [hasOfflineData, setHasOfflineData] = useState<boolean>(false);

  /** 경로 데이터 저장 */
  const savePathData = useCallback((data: OfflinePathData) => {
    try {
      localStorage.setItem(
        OFFLINE_PATH_STORAGE_KEY,
        JSON.stringify({
          ...data,
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
    if (isOnline) checkOfflineData();
  }, [isOnline, checkOfflineData]);

  return { hasOfflineData, savePathData, recoverOfflinePathData, loadOfflinePathData, discardOfflineData };
};
