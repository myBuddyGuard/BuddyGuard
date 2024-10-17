export type FilterType = 'weekly' | 'monthly' | 'all';

export type path = { lat: number; lng: number };

export type record = {
  id: number;
  buddyIds: [string, string];
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  totalTime: string;
  note: string;
  centerPosition: [string, string];
  mapLevel: number;
  path: path[];
  distance: number;
  pathImage: string;
};
export interface Buddy {
  id: number;
  image: string;
}

export interface WalkData {
  date: string;
  distance: number;
  totalTime: string;
  buddys: Buddy[];
  pathImage: string;
}

export interface WalkMockData {
  count: number;
  averageDistance: number;
  averageTime: string;
  data: WalkData[];
}
