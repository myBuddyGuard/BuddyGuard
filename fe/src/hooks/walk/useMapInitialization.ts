import { message } from 'antd';
import { useEffect, useRef, useState } from 'react';

import { createMap, createMarker, getcurrentPosition, loadKakaoMapScript } from '@/helper/kakaoMapHelpers';
import { PositionType } from '@/types/map';

interface useMapInitializationProps {
  mapRef: React.MutableRefObject<HTMLDivElement | null>;
  setChangedPosition: React.Dispatch<React.SetStateAction<PositionType | null>>;
  updatePosition: (newPosition: PositionType) => void;
  resetPosition: () => void;
}

export const useMapInitialization = ({
  mapRef,
  setChangedPosition,
  updatePosition,
  resetPosition,
}: useMapInitializationProps) => {
  const [map, setMap] = useState<kakao.maps.Map | null>(null);
  const markerRef = useRef<kakao.maps.Marker | null>(null);

  useEffect(() => {
    const mapElement = mapRef.current;

    const initialize = async () => {
      await loadKakaoMapScript();

      const { result, message: errorMessage, position } = await getcurrentPosition();

      updatePosition(position);

      if (!(window.kakao && mapElement)) {
        throw new Error('🚨 Kakao map or map reference not available');
      }

      window.kakao.maps.load(() => {
        const mapInstance = createMap(position, mapRef, setChangedPosition);
        setMap(mapInstance);

        if (result) {
          const newMarker = createMarker(position, mapInstance);
          markerRef.current = newMarker;
        } else {
          message.warning(errorMessage);
        }
      });
    };

    if (!map) initialize();

    return () => {
      if (!map) return;

      // 마커 제거
      if (markerRef.current) {
        markerRef.current.setMap(null);
        markerRef.current = null;
      }

      // 지도 컨테이너 초기화
      if (mapElement) {
        mapElement.innerHTML = '';
      }

      // 위치 상태 초기화
      resetPosition();
      setChangedPosition(null);

      // 지도 인스턴스 제거
      map.relayout();
      setMap(null);
    };
  }, [map, mapRef, setChangedPosition, updatePosition, resetPosition]);

  return { map, markerRef };
};
