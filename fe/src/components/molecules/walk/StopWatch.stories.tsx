import { Meta, StoryFn } from '@storybook/react';
import { useEffect, useState } from 'react';
import { useTheme } from 'styled-components';

import StopWatch, { StopWatchProps } from '@/components/molecules/walk/StopWatch';
import { createInitTimeRef } from '@/constants/map';
import { TimeRef } from '@/types/map';

export default {
  title: 'Molecules/StopWatch',
  component: StopWatch,
  argTypes: {
    status: {
      control: {
        type: 'select',
        options: ['start', 'pause', 'stop'],
      },
    },
  },
} as Meta;

const defaultTimeRef: TimeRef = createInitTimeRef();

// 템플릿 생성
const Template: StoryFn<StopWatchProps> = (args) => {
  const [status, setStatus] = useState<StopWatchProps['status']>(args.status);
  const [timeRef, setTimeRef] = useState(args.timeRef);
  const theme = useTheme();
  const bgColor = `${theme.themeValues.colorValues.grayscale[800]}`;

  useEffect(() => {
    setStatus(args.status);
    setTimeRef(args.timeRef);
  }, [args.status, args.timeRef]);

  return (
    <div style={{ backgroundColor: bgColor }}>
      <StopWatch {...args} status={status} timeRef={timeRef} />;
    </div>
  );
};

export const Default = Template.bind({});
Default.args = {
  status: 'start',
  timeRef: { current: defaultTimeRef },
  children: '시작',
};

export const Paused = Template.bind({});
Paused.args = {
  status: 'pause',
  timeRef: { current: defaultTimeRef },
  children: '일시정지',
};

export const Stopped = Template.bind({});
Stopped.args = {
  status: 'stop',
  timeRef: { current: defaultTimeRef },
  children: '종료',
};
