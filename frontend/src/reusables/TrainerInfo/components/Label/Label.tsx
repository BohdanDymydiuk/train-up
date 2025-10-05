import React from 'react';

import Image from 'next/image';

import styles from './Label.module.scss';

export const Label: React.FC = () => {
  return (
    <div className={styles.label}>
      <Image
        className={styles.celebration}
        src='/images/celebration.png'
        alt='Celebration'
      />
      <span className={styles.text}>Новий тренер</span>
    </div>
  );
};
