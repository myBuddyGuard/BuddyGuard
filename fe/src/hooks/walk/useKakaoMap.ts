import { useEffect, useState } from 'react';
import { RouteSnapshot, fromKakaoLatLng } from 'route-snap';

import { IsStartedType } from '@/components/pages/walk/GoWalk';
import { DEFAULT_MAP_LEVEL, DEFAULT_MAP_POSITION } from '@/constants/map';
import {
  adjustMapBounds,
  createOverLayElement,
  getMapPosition,
  moveMapTo,
  replaceCustomOverLay,
  setOverlay,
} from '@/helper/kakaoMapHelpers';
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
  const { map, mapRef, markerRef, changedPosition, setChangedPosition } = useKakaoMapInit({ positions, setPositions });

  // 사용자 위치 추적 로직
  const { watchID, startWatchingPosition, stopWatchingPosition, linePathRef, overlayRef } = useKakaoMapTracking({
    threshold,
    positions,
    setPositions,
    markerRef,
  });

  // 경로 그리기 로직
  const { handleDrawPolyline } = useKakaoMapDrawing({ map, linePathRef, positions });

  // 지도의 조작 및 제어 관련
  const { handleMapMoveAndStateUpdate, adjustMapBoundsToPath, handleTargetButtonClick } = useKakaoMapControls({
    map,
    positions,
    linePathRef,
    isTargetClicked,
    setIsTargetClicked,
    changedPosition,
    setChangedPosition,
    walkStatus,
  });

  // 오버레이 설정
  useEffect(() => {
    if (!(isStarted === 'start' && map && selectedBuddys.length && markerRef.current)) return;
    const { customContents, closeButton } = createOverLayElement(selectedBuddys, buddyList);
    setOverlay({ isStarted, selectedBuddys, overlayRef, markerRef, map, customContents, closeButton });
  }, [isStarted, map, selectedBuddys, buddyList]);

  // 산책 종료 후 경로 그리고 이미지 저장
  useEffect(() => {
    const donelogic = async () => {
      const linePath = linePathRef.current;
      console.log('linePath: ', linePath);
      if (!(canvasRef?.current instanceof HTMLCanvasElement)) return;

      const snapShot = new RouteSnapshot({
        canvasRef: { current: canvasRef.current },
        routes: fromKakaoLatLng(linePathRef.current),
      });

      const imageURL = snapShot.generate();

      if (!imageURL) return;

      await delay(1500);
      setIsStarted('done');
    };

    // 산책 종료 후 경로 그리고 이미지 저장
    if (walkStatus === 'stop' && mapRef.current && canvasRef.current && changedPosition) {
      donelogic();
    }
  }, [canvasRef, changedPosition, linePathRef, mapRef, walkStatus]);

  // 종료 버튼
  useEffect(() => {
    if (!(walkStatus === 'stop' && map && linePathRef.current && overlayRef.current)) return;
    // console.log('👽 1. 종료 버튼 누름');

    // 오버레이 제거
    if (overlayRef.current) {
      // console.log('👽 오버레이 제거');
      overlayRef.current.setMap(null);
    }
    // 위치 추적 중지
    if (watchID.current !== null) {
      // console.log('👽 위치추적 중지');
      stopWatchingPosition();
    }

    // bounds_changed 이벤트 리스너 추가
    const handleBoundsChanged = () => {
      // 지도가 실제로 업데이트된 후에 실행됨
      const newCenter = map.getCenter();
      // console.log('👽 3. 지도 범위가 설정된 후 중심 좌표 및 레벨 저장:', newCenter);
      setChangedPosition([newCenter.getLat(), newCenter.getLng()]);

      // 실행 후 리스너 제거 (한 번만 실행되도록)
      kakao.maps.event.removeListener(map, 'bounds_changed', handleBoundsChanged);
    };

    // 리스너 등록
    // console.log('👽 2. bounds_changed 이벤트 리스너 추가');
    kakao.maps.event.addListener(map, 'bounds_changed', handleBoundsChanged);

    adjustMapBounds(map, linePathRef.current);

    map.relayout();
  }, [map, walkStatus, stopWatchingPosition]);

  // 시작, 일시중지, 재시작
  useEffect(() => {
    // 시작 시 위치 업데이트 재개 + 마커의 새로운 위치로 오버레이 이동
    if (isStarted === 'start' && walkStatus === 'start' && map && selectedBuddys.length) {
      replaceCustomOverLay({ overlayRef, markerRef });

      // 이미 watchPosition이 실행 중인 경우 중복 호출 방지
      if (watchID.current === null) {
        handleMapMoveAndStateUpdate();
        startWatchingPosition(); // 위치 추적 재개
      }
    }

    // 일시 중지 시 위치 추적 중단
    if (walkStatus === 'pause' && watchID.current !== null) {
      stopWatchingPosition();
    }
  }, [
    isStarted,
    walkStatus,
    map,
    selectedBuddys,
    handleMapMoveAndStateUpdate,
    startWatchingPosition,
    stopWatchingPosition,
  ]);

  // 위치가 변경되었을 때 지도 중심 이동 (지도 다시 초기화하지 않음)
  useEffect(() => {
    if (map && positions.previous) {
      const moveLatLon = getMapPosition(positions);
      setChangedPosition(() => [positions.current[0], positions.current[1]]);
      moveMapTo(map, moveLatLon, DEFAULT_MAP_LEVEL);
    }
  }, [positions, map]);

  // 첫 지도 셋팅
  // useEffect(() => {
  //   return () => {
  //     // 필수적인 cleanup만 남기기
  //     if (map) {
  //       // 이벤트 리스너 제거
  //       kakao.maps.event.removeListener(map, 'center_changed', () =>
  //         centerChangedEventListener(map, setChangedPosition)
  //       );

  //       // 마커 제거
  //       if (markerRef.current) {
  //         markerRef.current.setMap(null);
  //         markerRef.current = null;
  //       }

  //       // 오버레이 제거
  //       if (overlayRef.current) {
  //         overlayRef.current.setMap(null);
  //         overlayRef.current = null;
  //       }

  //       // 지도 컨테이너 초기화
  //       if (mapRef.current) {
  //         mapRef.current.innerHTML = '';
  //       }
  //       // 위치 추적 중지
  //       if (watchID.current !== null) {
  //         navigator.geolocation.clearWatch(watchID.current);
  //         watchID.current = null;
  //       }

  //       // polyline 제거
  //       if (linePathRef.current.length > 0) {
  //         linePathRef.current = [];
  //       }
  //       // 상태 초기화
  //       setPositions({ previous: null, current: DEFAULT_MAP_POSITION });
  //       setChangedPosition(null);

  //       // 지도 인스턴스 제거
  //       map.relayout();
  //       setMap(null);
  //     }
  //   };
  // }, [mapRef, map]);

  return { map, mapRef, linePathRef, changedPosition };
};
