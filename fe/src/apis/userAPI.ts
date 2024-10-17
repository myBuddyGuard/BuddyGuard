import apiClient from '@/apis/axiosInstance';

const USER_BASE_URL = '/user';

export const getUserInfo = async () => {
  try {
    const response = await apiClient.get(`${USER_BASE_URL}`);
    console.log(response)
    return response.data;
  } catch (error) {
    console.error(error);
  }
};