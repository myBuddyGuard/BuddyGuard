import { create } from 'zustand';

type PetInfo = {
  userId: number;
  petId: number;
  petName: string;
  profileImage: string;
};

type PetState = {
  petsInfo: PetInfo[];
  setPetsInfo: (pets: PetInfo[]) => void;
};

export const usePetStore = create<PetState>((set) => ({
  petsInfo: [],
  setPetsInfo: (pets) => set({ petsInfo: pets }),
}));
