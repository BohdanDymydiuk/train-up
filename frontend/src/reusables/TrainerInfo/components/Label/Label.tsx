import React from 'react';

import { ALT_STRINGS, TRAINER_STRINGS } from '@/constants/strings';

import Image from 'next/image';

import styles from './Label.module.scss';

export const Label: React.FC = () => {
  return (
    <div className={styles.label}>
      <Image
        className={styles.celebration}
        src='/images/celebration.png'
        alt={ALT_STRINGS.celebration}
      />
      <span className={styles.text}>{TRAINER_STRINGS.newTrainer}</span>
    </div>
  );
};
