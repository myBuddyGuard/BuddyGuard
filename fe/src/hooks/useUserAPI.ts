import { useQuery } from '@tanstack/react-query';

import { getUserInfo } from '@/apis/userAPI';

export const useUserInfoQuery = () => {
  return useQuery({
    queryKey: ['userInfo'],
    queryFn: getUserInfo,
    // staleTime: 1000 * 60 * 5,
    // cacheTime: 1000 * 60 * 10,
    // onError: (error) => {
    //   console.error('Error fetching user info:', error);
    // },
  });
};
