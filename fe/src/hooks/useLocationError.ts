import { useState } from 'react';

import { CustomLocationErrorState } from '@/types/location';

const isGeolocationError = (error: unknown): error is GeolocationPositionError => {
  return (
    error !== null &&
    typeof error === 'object' &&
    'code' in error &&
    'PERMISSION_DENIED' in error &&
    'POSITION_UNAVAILABLE' in error &&
    'TIMEOUT' in error
  );
};

export const useLocationError = () => {
  const [locationError, setLocationError] = useState<CustomLocationErrorState | null>(null);

  const handleLocationError = (error: unknown) => {
    if (isGeolocationError(error)) {
      switch (error.code) {
        case error.PERMISSION_DENIED:
          setLocationError({
            code: 'PERMISSION_DENIED',
            message: '위치 권한이 거부 되었습니다.',
            action: 'setting',
          });
          break;
        case error.POSITION_UNAVAILABLE:
          setLocationError({
            code: 'POSITION_UNAVAILABLE',
            message: '위치를 가져올 수 없습니다.',
            action: 'retry',
          });
          break;
        case error.TIMEOUT:
          setLocationError({
            code: 'TIMEOUT',
            message: '시간이 초과되었습니다.',
            action: 'retry',
          });
          break;
        default:
          setLocationError({
            code: 'UNKNOWN_ERROR',
            message: '알 수 없는 위치 오류가 발생했습니다.',
            action: 'home',
          });
          console.error('Unexpected GeolocationPositionError:', error);
      }
      return;
    }

    if (error === 'RETRY_FAILED') {
      setLocationError({
        code: 'RETRY_FAILED',
        message: '위치 요청 재시도에 실패하였습니다.',
        action: 'home',
      });
    } else {
      setLocationError({
        code: 'UNKNOWN_ERROR',
        message: '알 수 없는 위치 오류가 발생했습니다.',
        action: 'home',
      });
      console.error('Unexpected GeolocationPositionError:', error);
    }
  };

  const clearLocationError = () => setLocationError(null);

  return { locationError, handleLocationError, clearLocationError };
};
