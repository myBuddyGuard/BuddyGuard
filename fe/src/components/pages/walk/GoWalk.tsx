import { message } from 'antd';
import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import WalkBuddySelectBar from '@/components/molecules/walk/WalkBuddySelectBar';
import WalkSatusBar from '@/components/molecules/walk/WalkSatusBar';
import WalkModal from '@/components/organisms/walk/WalkModal';
import { useKakaoMap } from '@/hooks/walk/useKakaoMap';
import { useWalkBuddys } from '@/hooks/walk/useWalkBuddys';
import { useWalkTime } from '@/hooks/walk/useWalkTime';
import { fillAvailable } from '@/styles/layoutStyles';

import WalkMap from './WalkMap';

export type IsStartedType = 'ready' | 'start' | 'done';

export default function GoWalk({ threshold }: { threshold: number | undefined }) {
  const navigate = useNavigate();
  const [isMapLoadError, setIsMapLoadError] = useState(false);
  const [isTargetClicked, setIsTargetClicked] = useState(false);

  // 1. 시간 관련
  const { timeRef, walkStatus, setWalkStatus, startTime } = useWalkTime();
  // 2. 버디 선택 관련
  const { buddyList, selectBuddy, selectedBuddys } = useWalkBuddys();
  // 3. 산책 상태 관리
  const [isStarted, setIsStarted] = useState<IsStartedType>('ready');
  // 4. 지도 캡처 관련
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  // 5. 카카오맵 관련
  const { map, mapRef, linePathRef, changedPosition } = useKakaoMap({
    threshold,
    buddyList,
    selectedBuddys,
    setIsMapLoadError,
    isTargetClicked,
    setIsTargetClicked,
    isStarted,
    setIsStarted,
    walkStatus,
    canvasRef,
  });

  const handleTargetClick = () => setIsTargetClicked((prev) => !prev);

  const handleStartIcon = () => {
    if (isMapLoadError) {
      message.error('현재 위치를 불러올 수 없습니다. \n 네트워크 또는 위치 권한 설정을 확인해주세요.');
      return;
    }

    if (!buddyList.length) {
      message.info('버디를 등록해주세요!');
      navigate('/MyPage/AddBuddy');
      return;
    }
    if (!selectedBuddys.length) {
      message.warning('산책할 버디를 선택해주세요!');
      return;
    }
    setIsStarted('start');
    startTime();
  };

  return (
    <StyledWalkWrapper>
      <WalkMap {...{ mapRef, isStarted, handleTargetClick, handleStartIcon }} />
      <canvas ref={canvasRef} style={{ display: 'none' }} /> {/* 캔버스는 숨김 */}
      {isStarted === 'ready' && <WalkBuddySelectBar {...{ buddyList, selectedBuddys, selectBuddy }} />}
      {isStarted === 'start' && <WalkSatusBar {...{ walkStatus, setWalkStatus, timeRef }} />}
      {isStarted === 'done' && (
        <WalkModal
          formTitle={'산책 완료'}
          timeRef={timeRef}
          linePathRef={linePathRef}
          selectedBuddys={selectedBuddys}
          buddyList={buddyList}
          canvasRef={canvasRef}
          changedPosition={changedPosition}
          map={map}
        />
      )}
    </StyledWalkWrapper>
  );
}

export const StyledWalkWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
  ${fillAvailable}
`;
