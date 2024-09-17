import React from 'react';
import Image from '../atoms/Image';
import Checkbox from '../atoms/Checkbox';

export default function Login() {
  return (
    <div style={{ padding: '2rem' }}>
      <Image src="/assets/icons/mascot.png" style={{ marginTop: '3rem', marginBottom: '3rem' }} />
      <div>
        <Image src="/assets/icons/kakao_login_medium_wide.png" style={{ marginBottom: '1rem' }} />
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Checkbox size="large" label="로그인 유지" />
        </div>
      </div>
    </div>
  );
}
