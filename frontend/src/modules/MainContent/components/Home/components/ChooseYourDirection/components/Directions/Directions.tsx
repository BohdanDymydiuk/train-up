import React, { useContext, useMemo } from 'react';

import { MainContext } from '../../../../../../../../context/MainContext';
import { useAppSelector } from '../../../../../../../../store/store';

import styles from './Directions.module.scss';

interface Props {
  areAllShown: boolean;
}

export const Directions: React.FC<Props> = ({ areAllShown }) => {
  const { onDesktop } = useContext(MainContext);

  const sports = useAppSelector(state => state.sports);

  const sportsShownNumber = useMemo(() => {
    return [...sports].slice(0, areAllShown ? sports.length : 6).length;
  }, [areAllShown, sports]);

  const rows = onDesktop ? Math.ceil(sportsShownNumber / 2) : sportsShownNumber;

  const heigthValue = `calc((var(--block-height) * ${rows}) + (var(--gap) * ${rows - 1}))`;

  return (
    <div className={styles.wrapper} style={{ height: heigthValue }}>
      {sports.map(sport => {
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
