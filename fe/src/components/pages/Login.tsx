import React from 'react';
import Image from '../atoms/Image';
import Div from '../atoms/Div';
import Checkbox from '../atoms/Checkbox';

export default function Login() {
  return (
    <Div style={{ padding: '2rem' }}>
      <Image src="/assets/icons/mascot.png" style={{ marginTop: '3rem', marginBottom: '3rem' }} />
      <Div>
        <Image src="/assets/icons/kakao_login_medium_wide.png" style={{ marginBottom: '1rem' }}></Image>
        <Checkbox size="large" label="로그인 유지" />
      </Div>
    </Div>
  );
}
