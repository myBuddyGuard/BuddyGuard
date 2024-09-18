import { Link } from 'react-router-dom';
import { useTheme } from 'styled-components';

import Div from '../atoms/Div';
import Image from '../atoms/Image';

export const NAV_HEIGHT = '4rem';

export default function Nav() {
  const theme = useTheme();
  const { backgroundPrimary: navBgColor, textPrimary: navTextColor } = theme.currentTheme;
  const commonStyle = { fontSize: '0.8rem', width: '1.5rem', color: navTextColor };

  return (
    <Div
      style={{
        display: 'flex',
        width: '100%',
        height: NAV_HEIGHT,
        justifyContent: 'space-around',
        alignItems: 'center',
        borderTop: '0.1rem solid black',
        backgroundColor: navBgColor,
      }}
    >
      <Link to="/">
        <Image src="/assets/icons/home.png" text="홈" textPosition="bottom" style={commonStyle} />
      </Link>
      <Link to="/menu/walk/go">
        <Image src="/assets/icons/walk.png" text="산책" textPosition="bottom" style={commonStyle} />
      </Link>
      <Link to="/menu">
        <Image src="/assets/icons/menu.png" text="메뉴" textPosition="bottom" style={commonStyle} />
      </Link>
      <Link to="/notification">
        <Image src="/assets/icons/notification.png" text="알림" textPosition="bottom" style={commonStyle} />
      </Link>
      <Link to="/MyPage">
        <Image src="/assets/icons/myPage.png" text="마이페이지" textPosition="bottom" style={commonStyle} />
      </Link>
    </Div>
  );
}