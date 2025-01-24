import { RoutePoint } from '../RouteSnapshot';

export interface KakaoLatLng {
  getLat(): number;
  getLng(): number;
}

export const fromKakaoLatLng = (path: KakaoLatLng[]): RoutePoint[] => {
  return path.map((point) => ({
    latitude: point.getLat(),
    longitude: point.getLng(),
  }));
};
