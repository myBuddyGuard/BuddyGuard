import { createBrowserRouter } from 'react-router-dom';

import AddFeed from '@/components/pages/feed/AddFeed';
import Feed from '@/components/pages/feed/Feed';
import Home from '@/components/pages/Home';
import AddHospital from '@/components/pages/hospital/AddHospital';
import AddVaccination from '@/components/pages/hospital/AddVaccination';
import Hospital from '@/components/pages/hospital/Hospital';
import Login from '@/components/pages/login/Login';
import Menu from '@/components/pages/Menu';
import AddBuddy from '@/components/pages/my/AddBuddy';
import MyBuddy from '@/components/pages/my/MyBuddy';
import MyPage from '@/components/pages/my/MyPage';
import NotFound from '@/components/pages/NotFound';
import Notification from '@/components/pages/Notification';
import Schedule from '@/components/pages/Schedule';
import GoWalk from '@/components/pages/walk/GoWalk';
import Walk from '@/components/pages/walk/Walk';
import Weight from '@/components/pages/Weight';
import ResponsiveLayout from '@/components/templates/ResponsiveLayout';
import RequireAuth from '@/routes/requireAuth';

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
    path: '/menu/walk/go',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk threshold={50} />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/test/no-threshold',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk threshold={undefined} />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/test/threshold-10',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk threshold={10} />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/test/threshold-50',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk threshold={50} />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/test/threshold-100',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <GoWalk threshold={100} />
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
    path: '/MyPage/AddBuddy',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <AddBuddy />
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
    path: '/menu/hospital',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Hospital />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/hospital/addHospital',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <AddHospital />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/hospital/addVaccination',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <AddVaccination />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/feed/addFeed',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <AddFeed />
        </ResponsiveLayout>
      </RequireAuth>
    ),
  },
  {
    path: '/menu/feed',
    element: (
      <RequireAuth>
        <ResponsiveLayout>
          <Feed />
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
