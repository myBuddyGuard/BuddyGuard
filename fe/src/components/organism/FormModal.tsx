import React from 'react';
import styled from 'styled-components';
import FormItem from '../molecule/FormItem';

interface FormModalProps {
  onClose: () => void;
  titleLabel?: string;
  dateLabel?: string;
  timeLabel?: string;
  formTitle: string;
  categoryTitle: string;
}

export default function FormModal({
  onClose,
  titleLabel,
  dateLabel,
  timeLabel,
  formTitle,
  categoryTitle,
}: FormModalProps) {
  return (
    <>
      <Overlay onClick={onClose} />

      <ModalContainer>
        <ModalHeader>
          <h3>{formTitle}</h3>
        </ModalHeader>

        <FormItemWrapper>
          <FormItem titleLabel={titleLabel} dateLabel={dateLabel} timeLabel={timeLabel} categoryTitle={categoryTitle} />
        </FormItemWrapper>

        <CloseButton onClick={onClose}>닫기</CloseButton>
      </ModalContainer>
    </>
  );
}

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
`;

const ModalContainer = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  width: 80%;
  max-width: 500px;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: center;
  padding: 1rem;
  h3 {
    font-weight: bold;
    font-size: 1.2rem;
  }
`;

const FormItemWrapper = styled.div`
  margin-bottom: 2rem;
`;

const CloseButton = styled.button`
  padding: 0.5rem 1rem;
  background-color: #ff5c5c;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  display: block;
  margin: 0 auto;
`;