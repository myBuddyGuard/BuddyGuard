import { useEffect } from 'react';

import BuddyInfoBar from '@/components/organisms/BuddyInfoBar';
import DashBoardList from '@/components/organisms/DashBoardList';
import { useAuthMutation } from '@/hooks/useAuthQuery';
import { usePetsInfoQuery } from '@/hooks/usePetAPI';
import { useUserInfoQuery } from '@/hooks/useUserAPI';
import { usePetStore } from '@/stores/usePetStore';
import { useUserInfoStore } from '@/stores/useUserInfoStore';

export default function Home() {
  const { getAccessToken, status } = useAuthMutation();
  const { data: userInfo, isSuccess: isUserInfoSuccess } = useUserInfoQuery();
  const { setUserInfo } = useUserInfoStore();

  const { data: petsData, isSuccess: isPetsInfoSuccess } = usePetsInfoQuery();
  const { setPetsInfo } = usePetStore();

  useEffect(() => {
    if (!localStorage.getItem('accessToken')) getAccessToken();
  }, [getAccessToken]);

  useEffect(() => {
    if (isUserInfoSuccess && userInfo) {
      setUserInfo(userInfo.userId, userInfo.petsId);
    }
  }, [isUserInfoSuccess, userInfo, setUserInfo]);

  useEffect(() => {
    if (isPetsInfoSuccess && petsData) {
      setPetsInfo(petsData);
    }
  }, [isPetsInfoSuccess, petsData, setPetsInfo]);

  if (status === 'pending') return <div>로그인 중입니다...</div>;

  if (status === 'error') return null;

  return (
    <div style={{ padding: '1rem' }}>
      <BuddyInfoBar />
      <div style={{ height: '75vh', overflowY: 'scroll', scrollbarWidth: 'none' }}>
        <DashBoardList />
      </div>
    </div>
  );
}
