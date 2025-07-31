import React from 'react';

import styles from './Label.module.scss';

export const Label: React.FC = () => {
  return (
    <div className={styles.label}>
      <img
        className={styles.celebration}
        src='/images/celebration.png'
        alt='Celebration'
      />
      <span className={styles.text}>Новий тренер</span>
    </div>
  );
};
