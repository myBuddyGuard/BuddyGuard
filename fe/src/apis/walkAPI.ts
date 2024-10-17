import apiClient from '@/apis/axiosInstance';
import { FilterType } from '@/types/walk';

const WALK_BASE_URL = '/api/walkRecords';
export const getWalkRecords = async (filterType: FilterType, buddyId: number, month?: number) => {
  try {
    const url =
      filterType !== 'weekly'
        ? `${WALK_BASE_URL}/${buddyId}/${filterType}/${month}`
        : `${WALK_BASE_URL}/${buddyId}/${filterType}`;

    const response = await apiClient.get(url);

    return response.data; // 응답 데이터 반환
  } catch (error) {
    console.error(error);
  }
};
