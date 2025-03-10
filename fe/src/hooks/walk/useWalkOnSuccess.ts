// useOnSuccess.ts
import { useQueryClient } from '@tanstack/react-query';
import { message } from 'antd';
import { useNavigate } from 'react-router-dom';

import { FilterType, successType } from '@/types/walk';

interface UseWalkOnSuccessProps {
  petId?: number;
  type?: FilterType;
  month?: number;
  onClose?: () => void;
}

export const useWalkOnSuccess = ({ onClose = () => {}, petId, type, month }: UseWalkOnSuccessProps) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateQueries = () => {
    if (petId && type === 'weekly') {
      queryClient.invalidateQueries({ queryKey: ['walkRecords', type, petId] });
    }

    if (petId && type === 'monthly' && month) {
      queryClient.invalidateQueries({ queryKey: ['walkRecords', type, petId, month] });
    }
  };

  return (actionType: successType) => {
    if (actionType === 'edit') message.success('🐶 수정되었습니다.');
    if (actionType === 'delete') message.success('🐶 삭제되었습니다.');
    if (actionType === 'save') message.success('🐶 등록에 성공하였습니다.');

    if (onClose) onClose();
    if (actionType === 'edit' || actionType === 'delete') invalidateQueries();

    navigate('/menu/walk');
  };
};
