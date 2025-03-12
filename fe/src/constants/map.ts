import { PositionType, TimeRef } from '@/types/map';

export const DEFAULT_MAP_POSITION: PositionType = [37.566418, 126.977943];
export const DEFAULT_MAP_LEVEL = 3;

export const createInitTimeRef = (): TimeRef => ({
  start: { day: new Date(), time: '' },
  end: { day: new Date(), time: '' },
  total: '',
});

export const OFFLINE_PATH_STORAGE_KEY = 'walkingPath';
