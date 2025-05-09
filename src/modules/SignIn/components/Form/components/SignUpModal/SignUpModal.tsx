import React from 'react';

import styles from './SignUpModal.module.scss';

interface Props {
  setIsModalShown: (value: boolean) => void;
}

export const SignUpModal: React.FC<Props> = ({ setIsModalShown }) => {
  const onClickHandler = () => setIsModalShown(false);

  return (
    <div className={styles['modal-wrapper']}>
      <div className={styles.modal}>
        <img
          src='/icons/cross.svg'
          alt='cross'
          className={styles.cross}
          onClick={onClickHandler}
        />
      </div>
    </div>
  );
};
