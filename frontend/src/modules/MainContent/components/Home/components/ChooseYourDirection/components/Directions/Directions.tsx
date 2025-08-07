import React from 'react';

import { useAppSelector } from '../../../../../../../../store/store';

import styles from './Directions.module.scss';

export const Directions: React.FC = () => {
  const sports = useAppSelector(state => state.sports);
  const sixSports = [...sports].slice(0, 6);

  return (
    <div className={styles.wrapper}>
      {sixSports.map(sport => {
        const { id, sportName } = sport;

        return (
          <div className={styles.block} key={id}>
            <div className={styles['first-part']}>
              <div className={styles['img-wrapper']}></div>
            </div>

            <h3 className={styles['sport-name']}>{sportName}</h3>
          </div>
        );
      })}
    </div>
  );
};
