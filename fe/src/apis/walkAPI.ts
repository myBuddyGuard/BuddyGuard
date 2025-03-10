import apiClient from '@/apis/axiosInstance';
import { MutationDeleteParams, MutationParams, UseWalkQueryProps } from '@/hooks/walk/useWalkQuery';

const WALK_BASE_URL = '/api/walkRecords';

export const getWalkRecords = async ({ filterKey, buddyId, month, year }: UseWalkQueryProps) => {
  try {
    let url = '';
    if (filterKey === 'weekly') url = `${WALK_BASE_URL}/${buddyId}/${filterKey}`;
    if (filterKey === 'monthly' && month) url = `${WALK_BASE_URL}/${buddyId}/${filterKey}/${month}`;
    if (filterKey === 'all' && month && year) url = `${WALK_BASE_URL}/${buddyId}/monthly/${month}?year=${year}`;

    const response = await apiClient.get(url);

    return response.data; // 응답 데이터 반환
  } catch (error) {
    console.error(error);
  }
};

/** 폼 데이터 저장 */
export const postWalkData = async (form: FormData) => {
  try {
    const { status } = await apiClient.post(WALK_BASE_URL, form);
    return status;
  } catch (error) {
    console.error('Upload failed:', error);
    throw error;
  }
};

/** 폼 데이터 수정
 * 200 ok
 */
export const patchWalkData = async ({ formData, petId, recordId }: MutationParams) => {
  try {
    const { status } = await apiClient.patch(`${WALK_BASE_URL}/${petId}/detail/${recordId}`, formData, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    return status;
  } catch (error) {
    console.error('edit failed:', error);
    throw error;
  }
};

/** 데이터 삭제 */
export const deleteWalkData = async ({ petId, recordId }: MutationDeleteParams) => {
  try {
    const { status } = await apiClient.delete(`${WALK_BASE_URL}/${petId}/detail/${recordId}`);
    return status;
  } catch (error) {
    console.error('delete failed:', error);
    throw error;
  }
};
