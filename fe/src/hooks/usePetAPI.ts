import { useQuery } from '@tanstack/react-query';

import { getPetInfo, getPetsInfo } from '@/apis/petAPI';

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
