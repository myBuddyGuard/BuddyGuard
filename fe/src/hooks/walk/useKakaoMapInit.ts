import { useEffect, useRef, useState } from 'react';

import { createMap, createMarker, getcurrentPosition, loadKakaoMapScript } from '@/helper/kakaoMapHelpers';
import { PositionPair, PositionType } from '@/types/map';

interface useKakaoMapInitProps {
  positions: PositionPair;
  setPositions: React.Dispatch<React.SetStateAction<PositionPair>>;
}
export const useKakaoMapInit = ({ positions, setPositions }: useKakaoMapInitProps) => {
  const [changedPosition, setChangedPosition] = useState<PositionType | null>(null);
  const [isMapScriptLoaded, setIsMapScriptLoaded] = useState(false);
  const [isPositionReady, setIsPositionReady] = useState(false);
  const markerRef = useRef<kakao.maps.Marker | null>(null);
  const mapRef = useRef<HTMLDivElement | null>(null);
  const [map, setMap] = useState<kakao.maps.Map | null>(null);

  // ✅ 1. 맵 스크립트 로드
  useEffect(() => {
    const loadScript = async () => {
      await loadKakaoMapScript();
      setIsMapScriptLoaded(() => true);
    };

    loadScript();
  }, []);

  // ✅ 2. 위치 가져오기 (맵 스크립트 로드 완료 후)
  useEffect(() => {
    // TODO: 위치 권한 상태 확인하고 없을 경우 예외 처리하기
    // const permissionStatus = await navigator.permissions.query({ name: 'geolocation' });
    if (!isMapScriptLoaded) return;

    const fetchLocation = async () => {
      const currentPosition = await getcurrentPosition();

      if (currentPosition.result === false) {
        console.error('Error fetching position:', currentPosition.message);
        return;
      }
      setPositions((prev) => ({ ...prev, current: currentPosition.position }));
      setIsPositionReady(() => true);
    };

    fetchLocation();
  }, [isMapScriptLoaded, setPositions]);

  // ✅ 3. 지도,마커 초기화 (위치 가져오기 완료 후)
  useEffect(() => {
    if (!(isPositionReady && window.kakao && mapRef.current)) return;

    window.kakao.maps.load(() => {
      const mapInstance = createMap(positions.current, mapRef, setChangedPosition);
      const newMarker = createMarker(positions.current, mapInstance);
      setMap(mapInstance);
      markerRef.current = newMarker;
    });

    return () => {
      if (markerRef.current) {
        markerRef.current.setMap(null);
        markerRef.current = null;
      }
    };
  }, [isPositionReady, positions]);

  return { map, mapRef, markerRef, changedPosition, setChangedPosition };
};
