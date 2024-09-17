import React from 'react';
import MenuItem from '../molecule/MenuItem';

export default function MenuList() {
  // api 완성되면 배열 갯수 대로 MenuItem mpa 돌릴예정
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '2rem', padding: '2rem' }}>
      <MenuItem />
      <MenuItem />
      <MenuItem />
      <MenuItem />
      <MenuItem />
      <MenuItem />
    </div>
  );
}
