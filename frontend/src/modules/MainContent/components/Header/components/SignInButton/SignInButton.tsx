import React from 'react';

import { ButtonProps } from '../../../../../../reusables/DropdownHoc';

import styles from './SignInButton.module.scss';

export const SignInButton: React.FC<ButtonProps> = ({ onClickHandler }) => {
  return (
    <button className={styles.signin} onClick={onClickHandler}>
      <div>Увійти як</div>
    </button>
  );
};
