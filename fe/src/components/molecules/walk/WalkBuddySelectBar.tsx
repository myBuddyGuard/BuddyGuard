import styled, { useTheme } from 'styled-components';

import Checkbox from '@/components/atoms/Checkbox';
import Image from '@/components/atoms/Image';
import Span from '@/components/atoms/Span';
import { NAV_HEIGHT } from '@/components/organisms/Nav';
import { BuddysType, CheckboxChangeHandler, SelectedBuddysType } from '@/types/map';
import mascot from '@public/assets/images/mascot.png';

export const BUDDY_SELECTBAR_HEIGHT = '10rem';

export interface WalkBuddySelectBarProps {
  buddyList: BuddysType[];
  selectedBuddys: SelectedBuddysType;
  selectBuddy: CheckboxChangeHandler;
}

export default function WalkBuddySelectBar({ buddyList, selectedBuddys, selectBuddy }: WalkBuddySelectBarProps) {
  const theme = useTheme();
  const spanColor = theme.currentTheme.textSubtle;

  const isChecked = (id: BuddysType['id']) => selectedBuddys.includes(id);

  return (
    <StyledSelectBar>
      <section>
        <Span style={{ color: spanColor }}>함께 산책할 버디를 선택해 주세요</Span>
      </section>

      <StyledSlideWrapper $buddyCount={buddyList.length || 0}>
        {buddyList.map(({ id, img, name }) => (
          <StyledBuddyWrapper key={`duddy-${id}`}>
            <StyledCheckbox
              checkBoxId={`${id}`}
              isChecked={isChecked(id)}
              handleOnChange={() => selectBuddy(id, !isChecked(id))}
            />
            <StyledImgWrapper>
              <Image
                style={{
                  width: '50%',
                  color: spanColor,
                  margin: '1rem 0 0.5rem 0',
                  border: '0.2rem solid white',
                  backgroundColor: 'beige',
                }}
                $borderRadius={'50%'}
                $isHover={false}
                src={img === 'none' ? mascot : img}
              ></Image>
              <StyledText style={{ color: spanColor }}>{name}</StyledText>
            </StyledImgWrapper>
          </StyledBuddyWrapper>
        ))}
      </StyledSlideWrapper>
    </StyledSelectBar>
  );
}

const StyledText = styled(Span)`
  white-space: nowrap;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
`;

const StyledImgWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const StyledCheckbox = styled(Checkbox)`
  z-index: 1;
  position: absolute;
  top: 0;
  right: 0;
`;

const StyledBuddyWrapper = styled.div`
  position: relative;
  width: 25%;
  flex-shrink: 0;
  margin-right: 1rem;
`;

const StyledSlideWrapper = styled.div<{ $buddyCount: number }>`
  margin-top: 1rem;
  display: flex;
  width: 100%;
  height: 80%;
  align-items: center;
  justify-content: ${({ $buddyCount }) => ($buddyCount < 4 ? `center` : `flex-start`)};

  overflow: overlay;
  -ms-overflow-style: none;
  scrollbar-width: thin;

  //TODO(Woody): 차이가 있는지 체감
  /* scroll-snap-type: x mandatory; */
  /* -webkit-overflow-scrolling: touch; */
`;

const StyledSelectBar = styled.div`
  z-index: 999;
  position: absolute;
  bottom: ${NAV_HEIGHT};
  width: 100%;
  height: ${BUDDY_SELECTBAR_HEIGHT};
  border: 0.15rem solid ${({ theme }) => theme.currentTheme.modalBackground};
  border-radius: 2rem 2rem 0 0;
  background-color: ${({ theme }) => theme.currentTheme.modalBackground2};
  padding: 1.2rem;
`;
