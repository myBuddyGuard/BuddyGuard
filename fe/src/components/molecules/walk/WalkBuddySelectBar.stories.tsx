import { Meta, StoryFn } from '@storybook/react';

import WalkBuddySelectBar, { WalkBuddySelectBarProps } from '@/components/molecules/walk/WalkBuddySelectBar';
import { StyledMobileFrame } from '@/components/templates/ResponsiveLayout';
import { BuddysType } from '@/types/map';
import image1 from '@public/images/profile01.png';
import image2 from '@public/images/profile02.png';
import image3 from '@public/images/profile03.png';

export default {
  title: 'Molecules/WalkBuddySelectBar',
  component: WalkBuddySelectBar,
  argTypes: {
    buddys: { control: 'object' },
    selectedBuddys: { control: 'object' },
    handleOnChange: { action: 'handleOnChange' },
  },
} as Meta;

const Template: StoryFn<WalkBuddySelectBarProps> = (args) => {
  return (
    <StyledMobileFrame>
      <WalkBuddySelectBar {...args} />
    </StyledMobileFrame>
  );
};

// 기본 데이터
const sampleBuddys: BuddysType[] = [
  { id: 1, img: image1, name: 'Buddy 1' },
  { id: 2, img: image2, name: 'Buddy 2' },
  { id: 3, img: image3, name: 'Buddy 3' },
];

export const Default = Template.bind({});
Default.args = {
  buddys: sampleBuddys,
  selectedBuddys: [],
  handleOnChange: () => {},
};

export const SelectBuddy = Template.bind({});
SelectBuddy.args = {
  buddys: sampleBuddys,
  selectedBuddys: [1, 2],
  handleOnChange: () => {},
};

export const ManyBuddys = Template.bind({});
ManyBuddys.args = {
  buddys: [
    ...sampleBuddys,
    { id: 4, img: image1, name: 'Buddy 4' },
    { id: 5, img: image2, name: 'Buddy 5' },
    { id: 6, img: image3, name: 'Buddy 6' },
  ],
  selectedBuddys: [],
  handleOnChange: () => {},
};

export const LongNameBuddy = Template.bind({});
LongNameBuddy.args = {
  buddys: [{ id: 1, img: image1, name: 'Buddy with a very very long name that overflows' }],
  selectedBuddys: [],
  handleOnChange: () => {},
};
