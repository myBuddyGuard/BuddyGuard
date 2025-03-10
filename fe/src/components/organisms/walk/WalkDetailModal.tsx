import { message } from 'antd';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import Button from '@/components/atoms/Button';
import WalkDetailFormItem from '@/components/molecules/walk/WalkDetailFormItem';
import { NAV_HEIGHT } from '@/components/organisms/Nav';
import { FormDataPatchType } from '@/components/organisms/walk/WalkModal';
import { useWalkOnSuccess } from '@/hooks/walk/useWalkOnSuccess';
import { useWalkDeleteMutation, useWalkPatchMutation } from '@/hooks/walk/useWalkQuery';
import { usePetStore } from '@/stores/usePetStore';
import { theme } from '@/styles/theme';
import TrashIcon from '@/svg/trash.svg';
import { FilterType, path, record } from '@/types/walk';

export interface FormDataType {
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  totalTime: string;
  buddysId: number[];
  note: string;
  centerPosition: path;
  mapLevel: number;
  path: path[];
  pathImage: string; // 이미지 URL 또는 base64
  distance: number;
}

interface WalkDetailModalProps {
  detailRecords: record;
  setIsClickedDetail: React.Dispatch<React.SetStateAction<boolean>>;
  type: FilterType;
  month: number;
}

const initForm = (detailRecords: record) => {
  const { note } = detailRecords;

  return { note };
};

export default function WalkDetailModal({ detailRecords, setIsClickedDetail, type, month }: WalkDetailModalProps) {
  const themeColor = theme.colorValues;
  const defaultColor = themeColor.special.textForce;
  const defaultGray = themeColor.grayscale[200];

  const { handleSubmit, setValue } = useForm<FormDataPatchType>({
    defaultValues: initForm(detailRecords),
  });
  const onClose = () => setIsClickedDetail(false);

  const { selectedBuddy } = usePetStore();
  const navigate = useNavigate();

  const onSuccessFn = useWalkOnSuccess({ onClose, petId: selectedBuddy?.petId, type, month });

  const onErrorFn = () => {
    message.error('😿 수정에 실패하였습니다.');
    onClose();
    navigate('/menu/walk');
  };

  const patchMutation = useWalkPatchMutation({ onSuccessFn, onErrorFn });
  const deleteMutation = useWalkDeleteMutation({ onSuccessFn, onErrorFn });

  const getPetIdAndRecordId = () => {
    const petId = selectedBuddy?.petId;
    const recordId = detailRecords.id;

    return { petId, recordId };
  };

  const handleDelete = () => {
    const { petId, recordId } = getPetIdAndRecordId();
    if (!petId) return;
    deleteMutation.mutate({ petId, recordId });
  };

  const onSubmit = async (formData: FormDataPatchType) => {
    const { petId, recordId } = getPetIdAndRecordId();
    if (!petId) return;
    patchMutation.mutate({ formData, petId, recordId });
  };

  return (
    <>
      <Overlay onClick={onClose} />

      <ModalContainer>
        <ModalHeader $isBottomLine={true} $lineThick={'0.15rem'}>
          <h3>산책 상세 기록</h3>
        </ModalHeader>

        <FormItemWrapper>
          <WalkDetailFormItem detailRecords={detailRecords} setValue={setValue}></WalkDetailFormItem>
        </FormItemWrapper>

        <ButtonWrapper>
          <Button
            onClick={handleDelete}
            $bgColor={'transparent'}
            style={{ border: `0.3rem solid ${defaultGray}`, borderRadius: '1rem', width: '6rem' }}
          >
            <TrashIcon />
          </Button>
          <Button
            onClick={handleSubmit(onSubmit)}
            $bgColor={defaultColor}
            style={{ border: 'none', borderRadius: '1rem', color: 'white', fontSize: '1.2rem', fontWeight: 'bold' }}
          >
            저 장
          </Button>
        </ButtonWrapper>
      </ModalContainer>
    </>
  );
}

const ModalContainer = styled.div`
  position: absolute;
  bottom: ${NAV_HEIGHT};
  width: 100%;
  height: 70vh;

  min-width: 23rem;
  padding: 1.3rem 1.5rem;
  border-radius: 1rem;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 999;
  background-color: ${({ theme }) => theme.currentTheme.modalBackground2};
  border: 0.2rem solid ${({ theme }) => theme.currentTheme.modalBackground};

  display: flex;
  flex-direction: column;
  * {
    font-size: 0.9rem;
  }
`;

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
`;

const FormItemWrapper = styled.div`
  margin-bottom: 2rem;
  overflow-y: auto;
  height: 100%;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 1rem;
`;

const ModalHeader = styled.div<{ $isBottomLine: boolean; $lineThick: string }>`
  display: flex;
  justify-content: left;
  position: relative;
  text-align: center;
  margin-bottom: 3rem;

  & h3 {
    font-weight: bold;
    font-size: 1.5rem;
    color: ${({ theme }) => theme.currentTheme.textPrimary};
    margin-left: 1rem;
  }

  ${({ $lineThick, theme }) =>
    `
    &::after {
      content: '';
      position: absolute;
      bottom: -1.5rem;
      width: 100%;
      height:  ${$lineThick}; 
      background-color: ${theme.themeValues.colorValues.grayscale[300]};
      z-index: 1000;
    }
  `}
`;
