/** Haversine 공식을 이용한 거리 계산 (단위 km) */
export const calculateDistanceKM = (
  prevLat: number,
  prevLng: number,
  currentLat: number,
  currentLng: number
): number => {
  const R = 6371; // 지구 반지름 (단위: km)
  const dLat = ((currentLat - prevLat) * Math.PI) / 180; // 위도 차이 (radian)
  const dLng = ((currentLng - prevLng) * Math.PI) / 180; // 경도 차이 (radian)

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((prevLat * Math.PI) / 180) *
      Math.cos((currentLat * Math.PI) / 180) *
      Math.sin(dLng / 2) *
      Math.sin(dLng / 2);

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  const distance = R * c; // 두 지점 사이의 거리 (단위: km)
  return distance;
};

/** 총 이동 거리 계산 */
export const calculateTotalDistance = (path: kakao.maps.LatLng[]): number => {
  const totalDistance = path.reduce((acc, cur, idx) => {
    if (idx === path.length - 1) return acc;

    const prevLat = cur.getLat();
    const prevLng = cur.getLng();
    const currentLat = path[idx + 1].getLat();
    const currentLng = path[idx + 1].getLng();

    return (acc += calculateDistanceKM(prevLat, prevLng, currentLat, currentLng));
  }, 0);

  return parseFloat(totalDistance.toFixed(3));
};
