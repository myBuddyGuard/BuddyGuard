import React, { useState } from 'react';
import styled from 'styled-components';

import Image from '@/components/atoms/Image';
import Span from '@/components/atoms/Span';
import PageTitleBar from '@/components/molecules/PageTitleBar';
import { usePetStore } from '@/stores/usePetStore';

export default function MyBuddy() {
  const defaultProfileImage = '/assets/images/mascot.png';
  const { petsInfo } = usePetStore();
  const [selectedBuddy, setSelectedBuddy] = useState(petsInfo[0]);

  const handleSelectBuddy = (buddy) => {
    setSelectedBuddy(buddy);
  };

  return (
    <MyBuddyContainer>
      <PageTitleBar title="나의 버디" />

      <ProfileWrapper>
        <Image src={defaultProfileImage} style={{ width: '80%' }} alt="프로필 이미지" />
      </ProfileWrapper>

      <InfoSection>
        <InfoContent>
          <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem 0', color: 'orange' }}>
            {selectedBuddy.petName}
          </Span>
          <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem 0' }}>품종</Span>
        </InfoContent>
        <Image src={defaultProfileImage} style={{ width: '5rem', marginRight: '1rem' }} alt="프로필 이미지" />
      </InfoSection>

      <PetDetails>
        <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem', marginBottom: '1rem' }}>
          나의 버디 정보
        </Span>
        <div>
          <Span style={{ fontSize: '1.3rem', fontWeight: 'bold', padding: '0.5rem' }}>생일</Span>
          <Span style={{ fontSize: '1.3rem', fontWeight: 'bold', padding: '0.5rem' }}>몸무게</Span>
          <Span style={{ fontSize: '1.3rem', fontWeight: 'bold', padding: '0.5rem' }}>등등...</Span>
        </div>
      </PetDetails>

      <BuddyList>
        <Span style={{ fontSize: '1.5rem', fontWeight: 'bold', padding: '0.5rem', marginBottom: '1rem' }}>
          나의 버디들
        </Span>
        <div>
          {petsInfo.map((buddy) => (
            <BuddyButton
              key={buddy.petId}
              onClick={() => handleSelectBuddy(buddy)}
              selected={selectedBuddy.petId === buddy.petId}
            >
              {buddy.petName}
            </BuddyButton>
          ))}
        </div>
      </BuddyList>
    </MyBuddyContainer>
  );
}

const MyBuddyContainer = styled.div`
  padding: 1rem;
`;

const ProfileWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-right: 1rem;
`;

const InfoSection = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem;
  border-bottom: 1px solid #ddd;
`;

const InfoContent = styled.div`
  display: flex;
  flex-direction: column;
`;

const PetDetails = styled.div`
  display: flex;
  flex-direction: column;
  align-items: start;
  padding: 2rem;
  border-bottom: 1px solid #ddd;
`;

const BuddyList = styled.div`
  display: flex;
  flex-direction: column;
  align-items: start;
  padding: 2rem;
  border-bottom: 1px solid #ddd;
`;

const BuddyButton = styled.button<{ selected: boolean }>`
  padding: 1rem;
  margin-right: 1rem;
  background-color: ${(props) => (props.selected ? 'orange' : 'lightgray')};
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
`;
