import { useCallback, useState } from 'react';

import { DEFAULT_MAP_POSITION } from '@/constants/map';
import { PositionPair, PositionType } from '@/types/map';

export const useMapPosition = () => {
  // 현재/이전 위치
  const [positions, setPositions] = useState<PositionPair>({
    previous: null, // 초기에는 이전 위치가 없으므로 null
    current: DEFAULT_MAP_POSITION, // 기본 위치를 현재 위치로 설정
  });
  // 사용자가 직접 지도를 이동시켜 변경한 위치
  const [changedPosition, setChangedPosition] = useState<PositionType | null>(null);

  /** 위치 상태 초기화 */
  const resetPosition = useCallback(() => {
    setPositions({ previous: null, current: DEFAULT_MAP_POSITION });
  }, []);

  /** 위치 상태 업데이트 */
  const updatePosition = useCallback((newPosition: PositionType) => {
    setPositions((prev) => ({
      previous: prev.current,
      current: newPosition,
    }));
  }, []);

  return { positions, updatePosition, resetPosition, changedPosition, setChangedPosition };
};
