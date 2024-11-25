/** 권한 없음 | 위치 못 가져옴 | 시간 초과 | 재시도 실패 | 알수없는 오류*/
export type LocationErrorCode =
  | 'PERMISSION_DENIED' // GeolocationPositionError
  | 'POSITION_UNAVAILABLE' // GeolocationPositionError
  | 'TIMEOUT' // GeolocationPositionError
  | 'RETRY_FAILED'
  | 'UNKNOWN_ERROR';

export interface CustomLocationError {
  type: LocationErrorCode;
  message: string;
}
