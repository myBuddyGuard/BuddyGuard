import { useEffect, useState } from 'react';
import { RouteSnapshot, fromKakaoLatLng } from 'route-snap';

import { IsStartedType } from '@/components/pages/walk/GoWalk';
import { DEFAULT_MAP_POSITION } from '@/constants/map';
import { adjustMapBounds, createOverLayElement, replaceCustomOverLay, setOverlay } from '@/helper/kakaoMapHelpers';
import { BuddysType, PositionPair, SelectedBuddysType, StatusOfTime } from '@/types/map';
import { delay } from '@/utils/utils';

import { useKakaoMapControls } from './useKakaoMapControls';
import { useKakaoMapDrawing } from './useKakaoMapDrawing';
import { useKakaoMapInit } from './useKakaoMapInit';
import { useKakaoMapTracking } from './useKakaoMapTracking';

export interface UseKakaoMapProps {
  threshold: number | undefined;
  buddyList: BuddysType[];
  selectedBuddys: SelectedBuddysType;
  setIsMapLoadError: React.Dispatch<React.SetStateAction<boolean>>;
  isTargetClicked: boolean;
  setIsTargetClicked: React.Dispatch<React.SetStateAction<boolean>>;
  isStarted: IsStartedType;
  setIsStarted: React.Dispatch<React.SetStateAction<IsStartedType>>;
  walkStatus: StatusOfTime;
  canvasRef: React.MutableRefObject<HTMLCanvasElement | null>;
}

export interface SetOverlayProps {
  isStarted: IsStartedType;
  selectedBuddys: SelectedBuddysType;
  markerRef: React.MutableRefObject<kakao.maps.Marker | null>;
  overlayRef: React.MutableRefObject<kakao.maps.CustomOverlay | null>;
  map: kakao.maps.Map | null;
  customContents: HTMLDivElement;
  closeButton: HTMLImageElement;
}

// 지도 초기화, 위치 추적, 경로 그리기
export const useKakaoMap = ({
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
}: UseKakaoMapProps) => {
  const [positions, setPositions] = useState<PositionPair>({
    previous: null, // 초기에는 이전 위치가 없으므로 null
    current: DEFAULT_MAP_POSITION, // 기본 위치를 현재 위치로 설정
  });

  // 초기화 관련 로직
  const { map, mapRef, markerRef, changedPosition, setChangedPosition } = useKakaoMapInit({
    positions,
    setPositions,
    setIsMapLoadError,
  });

  // 사용자 위치 추적 로직
  const { watchID, startWatchingPosition, stopWatchingPosition, linePathRef, overlayRef } = useKakaoMapTracking({
    threshold,
    positions,
    setPositions,
    markerRef,
  });

  // 경로 그리기 로직
  useKakaoMapDrawing({ map, linePathRef, positions });

  // 지도의 조작 및 제어 관련
  const { handleMapMoveAndStateUpdate } = useKakaoMapControls({
    map,
    positions,
    linePathRef,
    isTargetClicked,
    setIsTargetClicked,
    changedPosition,
    setChangedPosition,
    walkStatus,
  });

  // 산책 종료 후 경로 그린 이미지 저장
  useEffect(() => {
    if (!(walkStatus === 'stop' && mapRef.current && canvasRef.current && changedPosition)) return;

    const captureRouteImage = async () => {
      try {
        if (!(canvasRef?.current instanceof HTMLCanvasElement)) return;

        const snapShot = new RouteSnapshot({
          canvasRef: { current: canvasRef.current },
          routes: fromKakaoLatLng(linePathRef.current),
        });

        const imageURL = snapShot.generate();
        if (!imageURL) return;

        await delay(1500);
        setIsStarted('done');
      } catch (error) {
        console.error('Error capturing route image:', error);
      }
    };

    captureRouteImage();
  }, [canvasRef, changedPosition, linePathRef, mapRef, walkStatus, setIsStarted]);

  // 종료 버튼
  useEffect(() => {
    if (!(walkStatus === 'stop' && map && linePathRef.current && overlayRef.current)) return;

    // 오버레이 제거
    const removeOverlay = () => {
      if (overlayRef.current) {
        overlayRef.current.setMap(null);
      }
    };

    // 위치 추적 중지
    const stopTracking = () => {
      if (watchID.current !== null) {
        stopWatchingPosition();
      }
    };

    // 중심점 변경 이벤트 처리( bounds_changed 이벤트 리스너 추가)
    const setupBoundsChangedListener = () => {
      // 지도가 실제로 업데이트된 후에 실행됨
      const handleBoundsChanged = () => {
        const newCenter = map.getCenter();
        setChangedPosition([newCenter.getLat(), newCenter.getLng()]);

        // 실행 후 리스너 제거 (한 번만 실행되도록)
        kakao.maps.event.removeListener(map, 'bounds_changed', handleBoundsChanged);
      };

      kakao.maps.event.addListener(map, 'bounds_changed', handleBoundsChanged);
    };

    removeOverlay();
    stopTracking();
    setupBoundsChangedListener();

    adjustMapBounds(map, linePathRef.current);
    map.relayout();
  }, [map, walkStatus, stopWatchingPosition, setChangedPosition, watchID]);

  // 시작, 일시중지, 재시작 - 목적별로 분리
  useEffect(() => {
    const handleWalkStart = () => {
      if (!(isStarted === 'start' && walkStatus === 'start' && map && selectedBuddys.length)) return;

      // 오버레이 설정
      const { customContents, closeButton } = createOverLayElement(selectedBuddys, buddyList);
      setOverlay({ isStarted, selectedBuddys, overlayRef, markerRef, map, customContents, closeButton });

      // 시작 시 위치 업데이트 재개 + 마커의 새로운 위치로 오버레이 이동
      replaceCustomOverLay({ overlayRef, markerRef });

      // 이미 watchPosition이 실행 중인 경우 중복 호출 방지
      if (watchID.current === null) {
        handleMapMoveAndStateUpdate();
        startWatchingPosition(); // 위치 추적 재개
      }
    };

    // 산책 일시 중지 처리 (위치 추적 중단)
    const handleWalkPause = () => {
      if (!(walkStatus === 'pause' && watchID.current !== null)) return;

      stopWatchingPosition();
    };

    handleWalkStart();
    handleWalkPause();

    return () => {
      if (watchID.current !== null) {
        stopWatchingPosition();
      }
    };
  }, [
    isStarted,
    walkStatus,
    map,
    selectedBuddys,
    handleMapMoveAndStateUpdate,
    startWatchingPosition,
    stopWatchingPosition,
    watchID,
  ]);

  return { map, mapRef, linePathRef, changedPosition };
};
