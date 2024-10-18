import React, { useState } from 'react';

import Button from '@/components/atoms/Button';
import Input from '@/components/atoms/Input';
import Span from '@/components/atoms/Span';
import { useCreatePetMutation } from '@/hooks/usePetAPI';
import { usePetStore } from '@/stores/usePetStore';
import { PetData } from '@/types/pet';

export default function AddBuddy() {
  const [formData, setFormData] = useState<PetData>({
    name: '',
    type: '',
    birth: '',
    profile_image: '',
  });

  const { setPetsInfo } = usePetStore();
  const createPetMutation = useCreatePetMutation();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    createPetMutation.mutate(formData, {
      onSuccess: (newPet) => {
        setPetsInfo((prevPets) => [...prevPets, newPet]);
      },
      onError: (error) => {
        console.error('버디 추가 실패:', error);
      },
    });
  };

  return (
    <div style={{ padding: '1rem' }}>
      <Span style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>새 버디 추가하기</Span>
      <form onSubmit={handleSubmit}>
        <Input name="name" value={formData.name} onChange={handleInputChange} placeholder="버디 이름" required />
        <Input
          name="type"
          value={formData.type}
          onChange={handleInputChange}
          placeholder="종 (개, 고양이 등)"
          required
        />
        <Input
          name="birth"
          value={formData.birth}
          onChange={handleInputChange}
          placeholder="생일 (YYYY-MM-DD)"
          required
        />
        <Input
          name="profile_image"
          value={formData.profile_image}
          onChange={handleInputChange}
          placeholder="프로필 이미지 URL"
        />
        <Button type="submit">버디 추가</Button>
      </form>
    </div>
  );
}
