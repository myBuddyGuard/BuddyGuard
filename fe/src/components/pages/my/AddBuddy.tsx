import React, { useState } from 'react';
import styled from 'styled-components';

import Button from '@/components/atoms/Button';
import Input from '@/components/atoms/Input';
import PageTitleBar from '@/components/molecules/PageTitleBar';
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
    <Container>
      <PageTitleBar title="내 정보" />

      <FormTitle>새로운 버디 추가</FormTitle>
      <Form onSubmit={handleSubmit}>
        <StyledInput name="name" value={formData.name} onChange={handleInputChange} placeholder="버디 이름" required />
        <StyledInput
          name="type"
          value={formData.type}
          onChange={handleInputChange}
          placeholder="종 (개, 고양이 등)"
          required
        />
        <StyledInput
          name="birth"
          value={formData.birth}
          onChange={handleInputChange}
          placeholder="생일 (YYYY-MM-DD)"
          required
        />
        <StyledInput
          name="profile_image"
          value={formData.profile_image}
          onChange={handleInputChange}
          placeholder="프로필 이미지 URL"
        />

        <StyledButton type="submit">버디 추가</StyledButton>
      </Form>
    </Container>
  );
}

const Container = styled.div`
  padding: 2rem;
  max-width: 500px;
  margin: 0 auto;
`;

const FormTitle = styled.h3`
  font-size: 1.8rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
`;

const StyledInput = styled(Input)`
  padding: 0.8rem;
  margin-bottom: 1rem;
  font-size: 1.2rem;
  border: 1px solid #ccc;
  border-radius: 0.4rem;
  &:focus {
    outline: none;
    border-color: orange;
  }
`;

const StyledButton = styled(Button)`
  padding: 1rem;
  background-color: orange;
  color: white;
  font-size: 1.2rem;
  border: none;
  border-radius: 0.4rem;
  cursor: pointer;
  &:hover {
    background-color: orange;
  }
`;
