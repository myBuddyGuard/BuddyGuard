import { useCallback, useEffect } from 'react';

import { DEFAULT_MAP_LEVEL } from '@/constants/map';
import { adjustMapBounds, getMapPosition, isPositionsDifferent, moveMapTo } from '@/helper/kakaoMapHelpers';
import { PositionPair, PositionType, StatusOfTime } from '@/types/map';

interface UseKakaoMapControlsProps {
  map: kakao.maps.Map | null;
  positions: PositionPair;
  linePathRef: React.MutableRefObject<kakao.maps.LatLng[]>;
  isTargetClicked: boolean;
  setIsTargetClicked: React.Dispatch<React.SetStateAction<boolean>>;
  changedPosition: PositionType | null;
  setChangedPosition: React.Dispatch<React.SetStateAction<PositionType | null>>;
  walkStatus: StatusOfTime;
}

export const useKakaoMapControls = ({
  map,
  positions,
  linePathRef,
  isTargetClicked,
  setIsTargetClicked,
  changedPosition,
  setChangedPosition,
  walkStatus,
}: UseKakaoMapControlsProps) => {
  /** 현재위치로 이동 및 위치 상태 업데이트 */
  const handleMapMoveAndStateUpdate = useCallback(() => {
    const moveLatLon = getMapPosition(positions);
    setIsTargetClicked(false);
    setChangedPosition([positions.current[0], positions.current[1]]);
    if (!map) return;
    moveMapTo(map, moveLatLon, DEFAULT_MAP_LEVEL);
  }, [map, setIsTargetClicked, positions, setChangedPosition]);

  /** 지도 경계 자동 조정 */
  const adjustMapBoundsToPath = useCallback(() => {
    if (!map || linePathRef.current.length === 0) return;
    adjustMapBounds(map, linePathRef.current);
  }, [map, linePathRef]);

  /** 타겟 버튼 클릭 핸들러 */
  const handleTargetButtonClick = useCallback(() => {
    setIsTargetClicked(() => true);
  }, [setIsTargetClicked]);

  /** 지도 리사이즈 핸들러 */
  const handleMapResize = useCallback(() => {
    if (!map) return;
    map.relayout();
  }, [map]);

  // 리사이즈 이벤트 리스너
  useEffect(() => {
    window.addEventListener('resize', handleMapResize);
    return () => window.removeEventListener('resize', handleMapResize);
  }, [handleMapResize]);

  // 타겟 버튼 클릭 시 지도 중심 조정 또는 경로 자동 조정
  useEffect(() => {
    if (isTargetClicked && walkStatus === 'stop' && map) {
      adjustMapBoundsToPath();
      setIsTargetClicked(false);
      return;
    }

    if (isTargetClicked && isPositionsDifferent(positions, changedPosition) && map && walkStatus !== 'stop') {
      handleMapMoveAndStateUpdate();
    }
  }, [
    isTargetClicked,
    positions,
    changedPosition,
    map,
    walkStatus,
    handleMapMoveAndStateUpdate,
    adjustMapBoundsToPath,
    setIsTargetClicked,
  ]);

  return {
    handleMapMoveAndStateUpdate,
    adjustMapBoundsToPath,
    handleTargetButtonClick,
  };
};
