import { create } from 'zustand';

type UserState = {
  userId: number;
  petsId: number[];
};

type UserActions = {
  setUserInfo: (userId: number, petsId: number[]) => void;
};

export const useUserInfoStore = create<UserState & UserActions>((set) => ({
  userId: 0,
  petsId: [],

  setUserInfo: (userId, petsId) => set({ userId, petsId }),
}));
