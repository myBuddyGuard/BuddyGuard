import PageTitleBar from '@/components/molecules/PageTitleBar';
import MyPageList from '@/components/organisms/MyPageList';
import UserInfoBar from '@/components/organisms/UserInfoBar';
import { useUserInfoQuery } from '@/hooks/useUserAPI';

export default function MyPage() {
  const { data: userInfo, isSuccess } = useUserInfoQuery();

  if (!isSuccess || !userInfo) {
    return <div>Loading...</div>;
  }

  const { userId, name, email, profileImages, petsId } = userInfo;

  return (
    <div style={{ padding: '1rem' }}>
      <PageTitleBar title="내 정보" />
      <UserInfoBar name={name} email={email} profileImage={profileImages} />
      <MyPageList />
    </div>
  );
}
