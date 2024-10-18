import { useMutation, useQuery } from '@tanstack/react-query';

import { getPetInfo, getPetsInfo, createPet } from '@/apis/petAPI';

import { PetData } from '../types/pet';

// 전체 버디 정보
export const usePetsInfoQuery = () => {
  return useQuery({
    queryKey: ['petsInfo'],
    queryFn: getPetsInfo,
  });
};

// 특정 버디 정보
export const usePetInfoQuery = (petId: number) => {
  return useQuery({
    queryKey: ['petInfo', petId],
    queryFn: () => getPetInfo(petId),
  });
};

// 버디 추가하기
export const useCreatePetMutation = () => {
  return useMutation({
    mutationFn: (petData: PetData) => createPet(petData),
  });
};
