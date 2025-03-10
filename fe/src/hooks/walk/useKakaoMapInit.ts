import { useEffect, useRef, useState } from 'react';

import { createMap, createMarker, getcurrentPosition, loadKakaoMapScript } from '@/helper/kakaoMapHelpers';
import { PositionPair, PositionType } from '@/types/map';

interface useKakaoMapInitProps {
  positions: PositionPair;
  setPositions: React.Dispatch<React.SetStateAction<PositionPair>>;
  setIsMapLoadError: React.Dispatch<React.SetStateAction<boolean>>;
}
export const useKakaoMapInit = ({ positions, setPositions, setIsMapLoadError }: useKakaoMapInitProps) => {
  const [changedPosition, setChangedPosition] = useState<PositionType | null>(null);
  const [isMapScriptLoaded, setIsMapScriptLoaded] = useState(false);
  const [isPositionReady, setIsPositionReady] = useState(false);
  const markerRef = useRef<kakao.maps.Marker | null>(null);
  const mapRef = useRef<HTMLDivElement | null>(null);
  const [map, setMap] = useState<kakao.maps.Map | null>(null);

  // ✅ 1. 맵 스크립트 로드
  useEffect(() => {
    const loadScript = async () => {
      try {
        await loadKakaoMapScript();
        setIsMapScriptLoaded(true);
      } catch (error) {
        console.error('카카오맵 스크립트 로드 실패:', error);
        setIsMapLoadError(true);
      }
    };

    loadScript();
  }, [setIsMapLoadError]);

  // ✅ 2. 위치 가져오기 (맵 스크립트 로드 완료 후)
  useEffect(() => {
    if (!isMapScriptLoaded) return;

    const fetchLocation = async () => {
      const currentPosition = await getcurrentPosition();

      if (currentPosition.result === false) {
        console.error('위치 정보 가져오기 실패:', currentPosition.message);
        setIsMapLoadError(true);
      }

      // 성공이든 실패든 위치 정보 설정 및 준비 완료 상태로 변경
      setPositions((prev) => ({ ...prev, current: currentPosition.position }));
      setIsPositionReady(true);
    };

    fetchLocation();
  }, [isMapScriptLoaded, setPositions, setIsMapLoadError]);

  // ✅ 3. 지도,마커 초기화 (위치 가져오기 완료 후)
  useEffect(() => {
    if (!(isPositionReady && window.kakao && mapRef.current)) return;

    try {
      window.kakao.maps.load(() => {
        const mapInstance = createMap(positions.current, mapRef, setChangedPosition);
        const newMarker = createMarker(positions.current, mapInstance);
        setMap(mapInstance);
        markerRef.current = newMarker;
      });
    } catch (error) {
      console.error('지도 초기화 중 오류 발생:', error);
      setIsMapLoadError(true);
    }

    return () => {
      if (markerRef.current) {
        markerRef.current.setMap(null);
        markerRef.current = null;
      }
    };
  }, [isPositionReady, positions, setIsMapLoadError]);

  return { map, mapRef, markerRef, changedPosition, setChangedPosition };
};
