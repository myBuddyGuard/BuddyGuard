import { useCallback, useRef } from 'react';

import { PositionPair, PositionType } from '@/types/map';
import { calculateDistanceKM } from '@/utils/mapUtils';

import { UseKakaoMapProps } from './useKakaoMap';

interface useKakaoMapTrackingProps {
  threshold: UseKakaoMapProps['threshold'];
  positions: PositionPair;
  setPositions: React.Dispatch<React.SetStateAction<PositionPair>>;
  markerRef: React.MutableRefObject<kakao.maps.Marker | null>;
}
export const useKakaoMapTracking = ({ threshold, positions, setPositions, markerRef }: useKakaoMapTrackingProps) => {
  const watchID = useRef<number | null>(null); // watchPosition ID
  const linePathRef = useRef<kakao.maps.LatLng[]>([]);
  const overlayRef = useRef<kakao.maps.CustomOverlay | null>(null);

  /** 위치를 받아와 업데이트하는 함수 */
  const handlePositionUpdate = useCallback(
    (position: GeolocationPosition) => {
      try {
        const updatedPosition: PositionType = [position.coords.latitude, position.coords.longitude];
        const newLatLng = new kakao.maps.LatLng(updatedPosition[0], updatedPosition[1]);

        // 첫 위치인 경우 무조건 추가
        if (linePathRef.current.length === 0) {
          linePathRef.current.push(newLatLng);
        }

        // 이전 위치와 거리 계산
        const prevPosition = positions.current;

        //TEST: 1. 임계값 없는 경우 (undefined or 0)
        if (!threshold) {
          // linePath에 좌표 추가
          linePathRef.current.push(newLatLng);

          // 마커와 오버레이 위치 업데이트
          markerRef.current?.setPosition(newLatLng);
          overlayRef.current?.setPosition(newLatLng);

          // 상태 업데이트
          setPositions((prev) => ({
            previous: prev.current,
            current: updatedPosition,
          }));
          return;
        }

        // 임계값이 있는 경우 거리 계산
        const distance = prevPosition
          ? calculateDistanceKM(prevPosition[0], prevPosition[1], updatedPosition[0], updatedPosition[1]) * 1000
          : null;

        // 위치 변화가 거리 임계 값 이상일 경우에만 업데이트
        if (distance && distance >= threshold) {
          // linePath에 좌표 추가
          linePathRef.current.push(newLatLng);

          // 마커와 오버레이 위치 업데이트
          markerRef.current?.setPosition(newLatLng);
          overlayRef.current?.setPosition(newLatLng);

          // 상태 업데이트
          setPositions((prev) => ({
            previous: prev.current,
            current: updatedPosition,
          }));
        }
      } catch (error) {
        console.error('Error fetching position:', error);
      }
    },
    [positions, setPositions, threshold, markerRef, overlayRef] // ref여기 넣는게 맞을까?
  );

  /** Geolocation API로 위치 감지 시작 */
  const startWatchingPosition = useCallback(() => {
    if (watchID && watchID?.current && navigator.geolocation) {
      watchID.current = navigator.geolocation.watchPosition(
        (position) => handlePositionUpdate(position),
        (error) => {
          console.error('Error fetching position', error);
        },
        {
          enableHighAccuracy: true, // 고정밀도 사용
          timeout: 10000, // 10초 내에 위치 정보 못 가져오면 실패 처리
          maximumAge: 0, // 캐시된 위치 정보 사용 안함
        }
      );
    } else {
      console.error('Geolocation API not supported by this browser.');
    }
  }, [handlePositionUpdate]);

  /** Geolocation API로 위치 감지 중단 */
  const stopWatchingPosition = useCallback(() => {
    if (watchID?.current === null) return;

    navigator.geolocation.clearWatch(watchID.current);
    watchID.current = null;
  }, []);

  return { watchID, startWatchingPosition, stopWatchingPosition, linePathRef, overlayRef };
};
