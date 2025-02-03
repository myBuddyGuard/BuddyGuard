import { useEffect, useState } from 'react';

import { PetInfo } from '@/stores/usePetStore';
import { BuddysType, CheckboxChangeHandler, SelectedBuddysType } from '@/types/map';

export const useWalkBuddys = () => {
  const [buddyList, setBuddyList] = useState<BuddysType[]>([{ id: 0, img: '', name: '' }]);
  const [selectedBuddys, setSelectedBuddys] = useState<SelectedBuddysType>([]); // 클릭한 버디

  useEffect(() => {
    const petsStorage = localStorage.getItem('petsStorage');
    if (!petsStorage) return;

    const buddysList = JSON.parse(petsStorage)?.state?.petsInfo?.map(({ petId, petName, profileImage }: PetInfo) => ({
      id: petId,
      img: profileImage,
      name: petName,
    }));

    if (buddysList) {
      setBuddyList(buddysList);
    }
  }, []);

  const selectBuddy: CheckboxChangeHandler = (selectId: number, isSelect) =>
    setSelectedBuddys((prev) => (isSelect ? [...prev, selectId] : prev.filter((buddyId) => buddyId !== selectId)));

  return { buddyList, selectBuddy, selectedBuddys };
};
