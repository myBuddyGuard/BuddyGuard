import { createBrowserRouter } from 'react-router-dom';

import Food from '@/components/pages/Food';
import Health from '@/components/pages/Health';
import Home from '@/components/pages/Home';
import Login from '@/components/pages/login/Login';
import Menu from '@/components/pages/Menu';
import NotFound from '@/components/pages/NotFound';
import Notification from '@/components/pages/Notification';
import Schedule from '@/components/pages/Schedule';
import GoWalk from '@/components/pages/walk/GoWalk';
import Walk from '@/components/pages/walk/Walk';
import Weight from '@/components/pages/Weight';
import ResponsiveLayout from '@/components/templates/ResponsiveLayout';
import RequireAuth from '@/routes/requireAuth';
import MyPage from '@/components/pages/my/MyPage';
import MyBuddy from '@/components/pages/my/MyBuddy';

export const router = createBrowserRouter([
  {
    path: '/',
    element: (
      <ResponsiveLayout>
        <Home />
      </ResponsiveLayout>
    ),
  },
  {
    path: '/menu/walk',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Walk />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Menu />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/notification',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Notification />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/MyPage',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <MyPage />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/MyPage/MyBuddy',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <MyBuddy />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/join',
    element: (
      <ResponsiveLayout>
        <Login />
      </ResponsiveLayout>
    ),
  },
  {
    path: '/menu/walk/go',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/schedule',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Schedule />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/health',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Health />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/walk',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Walk />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },

  {
    path: '/menu/food',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Food />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/weight',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Weight />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '*',
    element: (
      // <RequireAuth>
      <ResponsiveLayout>
        <NotFound />
      </ResponsiveLayout>
      // </RequireAuth>
    ),
  },
]);
