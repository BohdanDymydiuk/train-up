import React from 'react';

import { AUTH_STRINGS } from '@/constants/strings';

import styles from './Button.module.scss';

export const Button: React.FC = () => {
  return (
    <button type='submit' className={styles.signin}>
      {AUTH_STRINGS.signIn}
    </button>
  );
};
