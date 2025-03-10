import { renderHook, act } from '@testing-library/react-hooks';
import { vi } from 'vitest';

import { initTimeRef } from '@/components/pages/walk/GoWalk';
import { getTimeFormatString } from '@/helper/timerHelpers';
import useStopWatch from '@/hooks/walk/useStopWatch';
import { StatusOfTime, TimeRef } from '@/types/map';

// 함수 모킹 (테스트 중에 이 함수의 실제 구현에 의존하지 않고 테스트 하기위함)
vi.mock('@/helper/timerHelpers', () => ({
  getTimeFormatString: vi.fn(() => '00:00:10'),
}));

// 테스트 그룹 정의
describe('useStopWatch Hook', () => {
  let timeRef: React.MutableRefObject<TimeRef>;

  // 각 테스트 전에 실행
  beforeEach(() => {
    timeRef = { current: initTimeRef };
    // 가상 타이머를 사용하여 타이머를 제어
    vi.useFakeTimers();
  });

  // 각 테스트 후에 실행
  afterEach(() => {
    // 타이머를 초기화
    vi.clearAllTimers();
  });

  test('타이머가 "start" 상태에서 1초씩 증가한다', () => {
    // renderHook: 훅을 렌더링
    const { result } = renderHook(() => useStopWatch('start', timeRef));

    // act: 상태 변화를 시뮬레이션
    // 1초가 지났을 때 타이머가 1이 되는지 확인
    act(() => {
      //타이머를 1초 앞으로 진행
      vi.advanceTimersByTime(1000);
    });
    // expect: 결과 검증
    expect(result.current).toBe(1);

    // 2초가 지났을 때 타이머가 2가 되는지 확인
    act(() => {
      vi.advanceTimersByTime(1000);
    });
    expect(result.current).toBe(2);
  });

  test('타이머가 "pause" 상태이면 멈춘다', () => {
    const { result, rerender } = renderHook<{ status: StatusOfTime }, number>(
      ({ status }) => useStopWatch(status, timeRef),
      {
        initialProps: { status: 'start' as const },
      }
    );

    // 타이머가 2초 동안 증가한 후
    act(() => {
      vi.advanceTimersByTime(2000);
    });
    expect(result.current).toBe(2);

    // "pause"로 상태를 변경하고 다시 렌더링
    rerender({ status: 'pause' });

    // 타이머가 멈췄는지 확인
    act(() => {
      vi.advanceTimersByTime(2000);
    });
    expect(result.current).toBe(2); // 더 이상 증가하지 않음
  });

  test('타이머가 "stop" 상태에서 종료되고, timeRef에 총 시간이 기록된다', () => {
    const { result, rerender } = renderHook<{ status: StatusOfTime }, number>(
      ({ status }) => useStopWatch(status, timeRef),
      {
        initialProps: { status: 'start' as const },
      }
    );

    // 3초 동안 타이머 증가
    act(() => {
      vi.advanceTimersByTime(3000);
    });
    expect(result.current).toBe(3);

    // "stop" 상태로 변경하고 다시 렌더링
    rerender({ status: 'stop' });

    // 3을 인자로 사용해 호출되었는지를 확인
    expect(getTimeFormatString).toHaveBeenCalledWith(3); //
    // timeRef에 총 시간이 기록되는지 확인 (모킹한 함수의 리턴 값을 기대)
    expect(timeRef.current.total).toBe('00:00:10');
  });
});
