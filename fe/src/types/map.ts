export type SelectedBuddysType = number[];
export type BuddysType = { id: number; img: string; name: string };

export type StatusOfTime = 'start' | 'pause' | 'stop';

export type PositionType = [number, number];

export interface getcurrentLocationResultType {
  result: boolean;
  message: string;
  position: PositionType;
}

export interface PositionPair {
  previous: PositionType | null;
  current: PositionType;
}
export interface SelctedBuddy {
  img: string;
}

export interface CheckboxChangeHandler {
  (checkBoxId: number, isChecked: boolean): void;
}

export interface TimeRef {
  start: { day: Date; time: string };
  end: { day: Date; time: string };
  total: string;
}
