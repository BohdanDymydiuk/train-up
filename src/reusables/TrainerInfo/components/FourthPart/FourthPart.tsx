import React from 'react';

import { TrainerInfoType } from '../../../../types/TrainerInfoType';
import { StarSVG } from '../../../svgs/StarSVG';

import styles from './FourthPart.module.scss';

type Props = Pick<TrainerInfoType, 'reviews'>;

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
