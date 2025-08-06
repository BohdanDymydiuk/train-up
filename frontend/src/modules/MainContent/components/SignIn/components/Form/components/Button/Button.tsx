import React from 'react';

import styles from './Button.module.scss';

export const Button: React.FC = () => {
  return (
    <button type='submit' className={styles.signin}>
      Увійти
    </button>
  );
};
