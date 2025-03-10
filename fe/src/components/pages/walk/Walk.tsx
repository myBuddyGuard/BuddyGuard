import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

import Chart from '@/components/molecules/Chart';
import PageTitleBar from '@/components/molecules/PageTitleBar';
import PeriodFilter from '@/components/molecules/PeriodFilter';
import Statistics from '@/components/molecules/Statistics';
import BuddyInfoBar from '@/components/organisms/BuddyInfoBar';
import { NAV_HEIGHT } from '@/components/organisms/Nav';
import WalkCalendar from '@/components/organisms/walk/WalkCalendar';
import WalkDetailModal from '@/components/organisms/walk/WalkDetailModal';
import WalkList from '@/components/organisms/walk/WalkList';
import { useWalkQuery, UseWalkQueryProps } from '@/hooks/walk/useWalkQuery';
// import { testData } from '@/mocks/walkTest';
import { useFilterStore } from '@/stores/useFilterStore';
import { usePetStore } from '@/stores/usePetStore';
import { flexColumn } from '@/styles/layoutStyles';
import { FilterType, record } from '@/types/walk';

function isWeekly(type: FilterType): type is 'weekly' {
  return type === 'weekly';
}

function isMonthly(type: FilterType): type is 'monthly' {
  return type === 'monthly';
}

function isAll(type: FilterType): type is 'all' {
  return type === 'all';
}

export default function Walk() {
  const { type, month, setType, year } = useFilterStore();
  const { selectedBuddy } = usePetStore();
  const titlePetId = selectedBuddy?.petId || 0;

  const queryProps: UseWalkQueryProps = React.useMemo(() => {
    if (isWeekly(type)) return { filterKey: type, buddyId: titlePetId };
    if (isMonthly(type)) return { filterKey: type, buddyId: titlePetId, month };
    if (isAll(type)) return { filterKey: type, buddyId: titlePetId, month, year };
    throw new Error('Invalid filter type');
  }, [type, month, year, titlePetId]);

  const { data, isLoading } = useWalkQuery(queryProps);
  // const data = testData;

  const [isClickedDetail, setIsClickedDetail] = useState(false);
  const [selectedData, setSelectedData] = useState<record | null>(null);

  useEffect(() => {
    if (data?.records) {
      const today = dayjs().format('YYYY-MM-DD');
      const todayRecord = data.records.find((record: record) => record.startDate === today);
      setSelectedData(todayRecord || null);
    }
  }, [data]);

  useEffect(() => {
    setType('weekly');
  }, [setType]);

  const isWeeklyOrMonthly = isWeekly(type) || isMonthly(type);

  return (
    <StyledWalkContainer>
      <StyledStaticWrapper>
        <Section>
          <PageTitleBar title="산책 관리" />
        </Section>

        <Section>
          <BuddyInfoBar />
        </Section>

        <Section>
          <PeriodFilter />
        </Section>

        {isWeeklyOrMonthly && (
          <>
            <Section>
              <Statistics stats={data?.stats} />
            </Section>

            <Section $height={20} $responsiveHeight={30}>
              <Chart records={data?.records} />
            </Section>
          </>
        )}
      </StyledStaticWrapper>

      {isWeeklyOrMonthly && (
        <ListWrapper>
          <WalkList
            records={data?.records}
            type={type}
            setIsClickedDetail={setIsClickedDetail}
            setSelectedData={setSelectedData}
          />
        </ListWrapper>
      )}

      {type === 'all' && (
        <AllTypeWrapper>
          <CalendarSection>
            <WalkCalendar setSelectedData={setSelectedData} />
          </CalendarSection>
          <ListWrapper>
            <WalkList
              records={data?.records}
              selectedData={selectedData}
              type={type}
              setIsClickedDetail={setIsClickedDetail}
              setSelectedData={setSelectedData}
            />
          </ListWrapper>
        </AllTypeWrapper>
      )}

      {isClickedDetail && selectedData && (
        <WalkDetailModal
          detailRecords={selectedData}
          setIsClickedDetail={setIsClickedDetail}
          type={type}
          month={month}
        />
      )}
    </StyledWalkContainer>
  );
}

const StyledWalkContainer = styled.div`
  flex: 1;
  min-height: 0;
  padding-bottom: ${NAV_HEIGHT};
  background-color: ${({ theme }) => theme.currentTheme.backgroundPrimary};
  display: flex;
  flex-direction: column;
  overflow: hidden;
`;

const StyledStaticWrapper = styled.div`
  padding: 0.1rem 1rem;
  ${flexColumn}
`;

const Section = styled.div<{ $height?: number; $responsiveHeight?: number }>`
  flex-shrink: 0;
  height: ${({ $height }) => $height}vh;

  @media (min-width: 60rem) {
    height: ${({ $responsiveHeight, $height }) => $responsiveHeight || $height}vh;
  }
`;

const ListWrapper = styled.div`
  flex: 1;
  min-height: 0;
  overflow: hidden;
  padding: 1rem 1rem 0.5rem 1rem;
  background-color: ${({ theme }) => theme.currentTheme.grayLighter};
  ${flexColumn}
`;

const AllTypeWrapper = styled.div`
  flex: 1;
  min-height: 0;
  ${flexColumn}
  overflow: hidden;
`;

const CalendarSection = styled.div``;
