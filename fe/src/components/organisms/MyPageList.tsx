import MyPageItem from '../molecules/MyPageItem';

export default function MyPageList() {
  return (
    <div >
      <MyPageItem title="내 버디들" router="/MyPage/MyBuddy" />
      <MyPageItem title="버디 추가하기" router="/add-buddy" />
      <MyPageItem title="버디가드 추가하기" router="/add-group-member" />
      <MyPageItem title="로그아웃" router="/add-group-member" />
    </div>
  );
}