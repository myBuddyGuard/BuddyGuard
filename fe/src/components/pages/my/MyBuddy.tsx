import Image from '@/components/atoms/Image';
import Span from '@/components/atoms/Span';
import PageTitleBar from '@/components/molecules/PageTitleBar';

export default function MyBuddy() {
  const defaultProfileImage = '/assets/images/mascot.png';

  return (
    <div style={{ padding: '1rem' }}>
      <PageTitleBar title="나의 버디" />
      <div style={{ display: 'flex', justifyContent: 'center', marginRight: '1rem' }}>
        <Image src="/assets/images/mascot.png" style={{ width: '80%' }} />
      </div>
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: '2rem',
          borderBottom: '1px solid #ddd',
        }}
      >
        <div style={{ display: 'flex', flexDirection: 'column' }}>
          <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem 0', color: 'orange' }}>이름</Span>
          <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem 0' }}>품종</Span>
        </div>
        <Image src={defaultProfileImage} style={{ width: '5rem', marginRight: '1rem' }} alt="프로필 이미지" />
      </div>
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: '2rem',
          borderBottom: '1px solid #ddd',
        }}
      >
        <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem' }}>내 버디 정보</Span>
      </div>
    </div>
  );
}
