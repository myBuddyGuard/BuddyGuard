import { message } from 'antd';
import { useEffect, useRef, useState } from 'react';

import { createMap, createMarker, getcurrentLocation, loadKakaoMapScript } from '@/helper/kakaoMapHelpers';
import { PositionType } from '@/types/map';

interface useMapInitializationProps {
  mapRef: React.MutableRefObject<HTMLDivElement | null>;
  setChangedPosition: React.Dispatch<React.SetStateAction<PositionType | null>>;
}

export const useMapInitialization = ({ mapRef, setChangedPosition }: useMapInitializationProps) => {
  const [map, setMap] = useState<kakao.maps.Map | null>(null);
  const markerRef = useRef<kakao.maps.Marker | null>(null);

  useEffect(() => {
    const initialize = async () => {
      await loadKakaoMapScript();

      const { result, message: errorMessage, position } = await getcurrentLocation();

      if (!(window.kakao && mapRef.current)) {
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
  }, [map, mapRef, setChangedPosition]);

  return { map, markerRef };
};
