import { useEffect } from 'react';
import BuddyInfoBar from '@/components/organisms/BuddyInfoBar';
import DashBoardList from '@/components/organisms/DashBoardList';
import { useAuthMutation } from '@/hooks/useAuthQuery';
import { useUserInfoStore } from '@/stores/useUserInfoStore';
import { useUserInfoQuery } from '@/hooks/useUserAPI';


export default function Home() {
  const { getAccessToken, status } = useAuthMutation();
  const { data: userInfo, isSuccess } = useUserInfoQuery(); 
  const { setUserInfo, userId, petsId } = useUserInfoStore(); 
  console.log(userId)
  console.log(petsId)
  useEffect(() => {
    if (!localStorage.getItem('accessToken')) getAccessToken();
  }, [getAccessToken]);

  
  useEffect(() => {
    if (isSuccess && userInfo) {
      setUserInfo(userInfo.userId, userInfo.petsId); 
    }
  }, [isSuccess, userInfo, setUserInfo]);

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