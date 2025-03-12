import { message } from 'antd';
import { useEffect, useState } from 'react';

import { FormDataType } from '@/components/organisms/walk/WalkModal';

// 저장할 데이터 타입 정의
export type OfflinePathData = Omit<FormDataType, 'note' | 'pathImage'>;

export const useNetworkStatus = () => {
  const [isOnline, setIsOnline] = useState<boolean>(navigator.onLine);

  useEffect(() => {
    const handleOnline = () => {
      setIsOnline(true);
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
  }, []);

  return { isOnline };
};
