import { RefObject } from 'react';
import styled from 'styled-components';

import PlayIcon from '@/components/icons/PlayIcon';
import { BUDDY_SELECTBAR_HEIGHT } from '@/components/molecules/walk/WalkBuddySelectBar';
import { NAV_HEIGHT } from '@/components/organisms/Nav';
import { fillAvailable } from '@/styles/layoutStyles';
import targetIcon from '@public/assets/icons/targetIcon.png';

import { IsStartedType } from './GoWalk';

interface WalkMapProps {
  mapRef: RefObject<HTMLDivElement>;
  isStarted: IsStartedType;
  handleTargetClick: () => void;
  handleStartIcon: () => void;
}

const PLAY_ICON_GAP = '5rem';

export default function WalkMap({ mapRef, isStarted, handleTargetClick, handleStartIcon }: WalkMapProps) {
  const playIconStyle = {
    $stroke: 'white',
    $shadow: '2px 2px 5px rgba(0, 0, 0, 0.5)',
    $isCursor: true,
    $size: 110,
  };

  return (
    <>
      {isStarted === 'ready' && <StyledBlockLayer />}
      {isStarted === 'ready' && <StyledPlayIcon customStyle={playIconStyle} onClick={handleStartIcon} />}
      {isStarted === 'start' && (
        <StyledTargetIcon onClick={handleTargetClick}>
          <img src={targetIcon} />
        </StyledTargetIcon>
      )}
      <StyledMap ref={mapRef} />
    </>
  );
}

const StyledBlockLayer = styled.div`
  position: absolute;
  top: 0;
  z-index: 2;
  background-color: gray;
  opacity: 50%;
  min-width: 100%;
  min-height: 100%;
`;

const StyledPlayIcon = styled(PlayIcon)`
  position: absolute;
  z-index: 999;
  bottom: calc(${NAV_HEIGHT} + ${BUDDY_SELECTBAR_HEIGHT} + ${PLAY_ICON_GAP});
  left: 50%;
  transform: translateX(-50%);
`;

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

const StyledMap = styled.div`
  width: 100%;
  height: 100%;
  min-height: 400px; // 최소 높이 설정
  object-fit: cover;
  ${fillAvailable}
`;
