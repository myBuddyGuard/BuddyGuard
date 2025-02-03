import { message } from 'antd';
import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import PlayIcon from '@/components/icons/PlayIcon';
import WalkBuddySelectBar, { BUDDY_SELECTBAR_HEIGHT } from '@/components/molecules/walk/WalkBuddySelectBar';
import WalkSatusBar from '@/components/molecules/walk/WalkSatusBar';
import { NAV_HEIGHT } from '@/components/organisms/Nav';
import WalkModal from '@/components/organisms/walk/WalkModal';
import { useKakaoMap } from '@/hooks/walk/useKakaoMap';
import { useWalkBuddys } from '@/hooks/walk/useWalkBuddys';
import { useWalkTime } from '@/hooks/walk/useWalkTime';
import { fillAvailable } from '@/styles/layoutStyles';
import { PositionType } from '@/types/map';
import targetIcon from '@public/assets/icons/targetIcon.png';

const playIconStyle = {
  $stroke: 'white',
  $shadow: '2px 2px 5px rgba(0, 0, 0, 0.5)',
  $isCursor: true,
  $size: 110,
};

const PLAY_ICON_GAP = '5rem';

export type IsStartedType = 'ready' | 'start' | 'done';

export default function GoWalk({ threshold }: { threshold: number | undefined }) {
  // 1. 시간 관련
  const { timeRef, walkStatus, setWalkStatus, startTime } = useWalkTime();

  // 2. 버디 선택 관련
  const { buddyList, selectBuddy, selectedBuddys } = useWalkBuddys();

  // 3. 산책 상태 관리
  const [isStarted, setIsStarted] = useState<IsStartedType>('ready');

  // 4. 지도 캡처 관련
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [capturedImage, setCapturedImage] = useState<string | null>(null); // 캡처된 이미지를 저장할 상태

  // 5. 카카오맵 관련
  const mapRef = useRef<HTMLDivElement | null>(null);
  const linePathRef = useRef<kakao.maps.LatLng[]>([]);
  const [map, setMap] = useState<kakao.maps.Map | null>(null);
  const [changedPosition, setChangedPosition] = useState<PositionType | null>(null);
  const [isTargetClicked, setIsTargetClicked] = useState(false);

  const navigate = useNavigate();

  useKakaoMap({
    threshold,
    mapRef,
    buddyList,
    selectedBuddys,
    isTargetClicked,
    setIsTargetClicked,
    isStarted,
    setIsStarted,
    walkStatus,
    setCapturedImage,
    canvasRef,
    linePathRef,
    changedPosition,
    setChangedPosition,
    map,
    setMap,
  });

  const startGoWalk = () => {
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
      {isStarted === 'ready' && <StyledBlockLayer />}
      {isStarted === 'ready' && <StyledPlayIcon customStyle={playIconStyle} onClick={startGoWalk} />}
      {isStarted === 'start' && (
        <StyledTargetIcon onClick={() => setIsTargetClicked((prev) => !prev)}>
          <img src={targetIcon} />
        </StyledTargetIcon>
      )}
      <StyledMap ref={mapRef} />
      <canvas ref={canvasRef} style={{ display: 'none' }} /> {/* 캔버스는 숨김 */}
      {isStarted === 'start' && (
        <WalkSatusBar walkStatus={walkStatus} setWalkStatus={setWalkStatus} timeRef={timeRef} />
      )}
      {isStarted === 'ready' && (
        <WalkBuddySelectBar buddys={buddyList} selectedBuddys={selectedBuddys} handleOnChange={selectBuddy} />
      )}
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

const StyledTargetIcon = styled.div`
  position: absolute;
  z-index: 999;
  left: 1rem;
  bottom: calc(${NAV_HEIGHT} + ${BUDDY_SELECTBAR_HEIGHT});
  background-color: white;
  border: 0.2rem solid grey;
  width: 3rem;
  height: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 1rem;
  cursor: pointer;

  & img {
    width: 80%;
    height: 80%;
  }
`;

const StyledBlockLayer = styled.div`
  position: absolute;
  top: 0;
  z-index: 2;
  background-color: gray;
  opacity: 50%;
  min-width: 100%;
  min-height: 100%;
`;

const StyledMap = styled.div`
  width: 100%;
  height: 100%;
  min-height: 400px; // 최소 높이 설정
  object-fit: cover;
  ${fillAvailable}
`;

const StyledPlayIcon = styled(PlayIcon)`
  position: absolute;
  z-index: 999;
  bottom: calc(${NAV_HEIGHT} + ${BUDDY_SELECTBAR_HEIGHT} + ${PLAY_ICON_GAP});
  left: 50%;
  transform: translateX(-50%);
`;

export const StyledWalkWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
  ${fillAvailable}
`;
