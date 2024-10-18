import Image from '@/components/atoms/Image';
import Span from '@/components/atoms/Span';

export default function UserInfoBar({ name, email, profileImage }: { name: string, email: string, profileImage: string }) {
  const defaultProfileImage = '/assets/images/mascot.png';
  const imageToShow = profileImage || defaultProfileImage;

  return (
    <div
      style={{
        display:"flex",
        justifyContent:"space-between",
        alignItems:"center",
        marginBottom: '4rem',
        padding: '2rem',
        borderBottom: '1px solid #ddd',
      }}
    >
      <div>
        <Span style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{name}</Span>
        <div style={{ marginTop: '1rem' }}>
          <Image src="/assets/icons/mail.png" style={{ width: '1rem', marginRight: '0.5rem' }} />
          <Span>{email}</Span>
        </div>
      </div>
        <Image src={imageToShow} style={{ width: '5rem', marginRight: '1rem' }} alt="프로필 이미지" />
    </div>
  );
}