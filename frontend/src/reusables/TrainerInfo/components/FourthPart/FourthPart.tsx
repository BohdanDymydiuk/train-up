import React from 'react';

import { StarSVG } from '@/reusables/svgs/sectionSvgs/trainers/StarSVG';
import { Trainer } from '@/shared/types/trainer';

import styles from './FourthPart.module.scss';

type Props = Pick<Trainer, 'reviews'>;

export const FourthPart: React.FC<Props> = ({ reviews }) => {
  return (
    <div className={styles['fourth-part']}>
      <div className={styles.reviews}>
        <span className={styles.number}>{reviews}</span>
        <StarSVG />
      </div>
      <button className={styles.signup}>Записатись</button>
    </div>
  );
};
