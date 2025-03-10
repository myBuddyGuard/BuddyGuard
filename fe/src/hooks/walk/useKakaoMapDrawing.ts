import { useCallback, useEffect } from 'react';

import { createPolyline, drawPolylineOnMap } from '@/helper/kakaoMapHelpers';
import { PositionPair } from '@/types/map';

interface UseKakaoMapDrawingProps {
  map: kakao.maps.Map | null;
  linePathRef: React.MutableRefObject<kakao.maps.LatLng[]>;
  positions: PositionPair;
}

export const useKakaoMapDrawing = ({ map, linePathRef, positions }: UseKakaoMapDrawingProps) => {
  /** 선을 지도에 그리는 함수 */
  const handleDrawPolyline = useCallback(() => {
    if (map && linePathRef.current.length > 1) {
      const polyline = createPolyline(linePathRef.current);
      drawPolylineOnMap(map, polyline);
    }
  }, [map, linePathRef]);

  // 지도에 경로 그리기
  useEffect(() => {
    handleDrawPolyline();
  }, [positions, handleDrawPolyline]);

  return { handleDrawPolyline };
};
