import React from 'react';
import Span from '../atoms/Span';
import Image from '../atoms/Image';

export default function MenuItem() {
  return (
    <div>
      <Image src="/assets/icons/calendarIcon.png" />
      <Span>일정</Span>
    </div>
  );
}
