import React from 'react';
import { Link } from 'react-router-dom';
import Image from '../atoms/Image';

export default function Nav() {
  return (
    <div
      style={{
        display: 'flex',
        width: '100%',
        height: '4rem',
        justifyContent: 'space-around',
        alignItems: 'center',
        borderTop: '0.1rem solid black',
      }}
    >
      <Link to="/">
        <Image
          src="/assets/icons/home.png"
          text="홈"
          textPosition="bottom"
          style={{ fontSize: '0.8rem', width: '1.5rem' }}
        />
      </Link>
      <Link to="/menu/walk">
        <Image
          src="/assets/icons/walk.png"
          text="산책"
          textPosition="bottom"
          style={{ fontSize: '0.8rem', width: '1.5rem' }}
        />
      </Link>
      <Link to="/menu">
        <Image
          src="/assets/icons/menu.png"
          text="메뉴"
          textPosition="bottom"
          style={{ fontSize: '0.8rem', width: '1.5rem' }}
        />
      </Link>
      <Link to="/notification">
        <Image
          src="/assets/icons/notification.png"
          text="알림"
          textPosition="bottom"
          style={{ fontSize: '0.8rem', width: '1.5rem' }}
        />
      </Link>
      <Link to="/MyPage">
        <Image
          src="/assets/icons/myPage.png"
          text="마이페이지"
          textPosition="bottom"
          style={{ fontSize: '0.8rem', width: '1.5rem' }}
        />
      </Link>
    </div>
  );
}
