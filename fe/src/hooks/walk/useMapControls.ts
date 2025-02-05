import { useCallback, useEffect, useRef, useState } from 'react';

import { DEFAULT_MAP_LEVEL } from '@/constants/map';
import {
  adjustMapBounds,
  createPolyline,
  drawPolylineOnMap,
  getMapPosition,
  isPositionsDifferent,
  moveMapTo,
} from '@/helper/kakaoMapHelpers';
import { PositionPair, PositionType, StatusOfTime } from '@/types/map';

interface useMapContorlsProps {
  map: kakao.maps.Map | null;
  positions: PositionPair;
  changedPosition: PositionType | null;
  setChangedPosition: React.Dispatch<React.SetStateAction<PositionType | null>>;
  walkStatus: StatusOfTime;
}
export const useMapContorls = ({
  map,
  positions,
  changedPosition,
  setChangedPosition,
  walkStatus,
}: useMapContorlsProps) => {
  const [isTargetClicked, setIsTargetClicked] = useState(false);
  const pathRef = useRef<kakao.maps.LatLng[]>([]);

  /** 타켓 클릭 핸들러  */
  const handleTargetClick = useCallback(() => setIsTargetClicked((prev) => !prev), []);

  /** 해당 위치로 지도 위치 변경 */
  const moveMapToLocation = useCallback(
    (targetPosition: PositionPair) => {
      if (!map) return;
      const moveLatLon = getMapPosition(targetPosition);
      moveMapTo(map, moveLatLon, DEFAULT_MAP_LEVEL);
    },
    [map]
  );

  /** 현재위치로 이동 및 위치 상태 업데이트 */
  const moveMapToCurrent = useCallback(() => {
    setIsTargetClicked(false);
    setChangedPosition([positions.current[0], positions.current[1]]);
    moveMapToLocation(positions);
  }, [setIsTargetClicked, positions, setChangedPosition, moveMapToLocation]);

  /** 지도에 경로 그리기 */
  const drawPath = useCallback(() => {
    if (!map || pathRef.current.length < 2) return;
    const polyline = createPolyline(pathRef.current);
    drawPolylineOnMap(map, polyline);
  }, [map, pathRef]);

  // 지도 리사이즈 처리
  useEffect(() => {
    const handleResize = () => {
      if (!map) return;
      map.relayout();
    };

    window.addEventListener('resize', handleResize);

    return () => window.removeEventListener('resize', handleResize);
  }, [map]);

  // 위치 변경에 따른 지도 이동
  useEffect(() => {
    if (!map || !positions.previous) return;
    moveMapToLocation(positions);
    setChangedPosition([positions.current[0], positions.current[1]]);
  }, [positions, map, setChangedPosition, moveMapToLocation]);

  // 타겟 버튼 동작 처리
  useEffect(() => {
    if (!isTargetClicked || !map) return;

    if (walkStatus === 'stop') {
      adjustMapBounds(map, pathRef.current);
      setIsTargetClicked(false);
      return;
    }

    if ((walkStatus === 'start' || walkStatus === 'pause') && isPositionsDifferent(positions, changedPosition)) {
      moveMapToCurrent();
    }
  }, [isTargetClicked, positions, changedPosition, map, walkStatus, moveMapToCurrent, setIsTargetClicked]);

  // 지도에 경로 그리기
  useEffect(() => {
    drawPath();
  }, [positions, drawPath]);

  return { moveMapToCurrent, drawPath, pathRef, handleTargetClick };
};
