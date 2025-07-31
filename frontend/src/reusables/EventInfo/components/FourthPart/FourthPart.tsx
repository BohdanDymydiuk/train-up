import React from 'react';

import { EventInfoType } from '../../../../types/EventInfoType';

import styles from './FourthPart.module.scss';

type Props = Pick<EventInfoType, 'trainer'>;

export const FourthPart: React.FC<Props> = ({ trainer }) => {
  return (
    <div className={styles.trainer}>
      <div className={styles.avatar} />
      <span className={styles.text}>{trainer}</span>
    </div>
  );
};
