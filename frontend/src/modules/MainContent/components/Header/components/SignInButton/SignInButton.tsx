import React from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { ButtonProps } from '@/reusables/DropdownHoc';

import styles from './SignInButton.module.scss';

export const SignInButton: React.FC<ButtonProps> = ({ onClickHandler }) => {
  return (
    <button className={styles.signin} onClick={onClickHandler}>
      <div>{AUTH_STRINGS.signInAs}</div>
    </button>
  );
};
