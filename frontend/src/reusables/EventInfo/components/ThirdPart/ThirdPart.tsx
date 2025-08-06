import React, { useMemo } from 'react';

import { EventInfoType } from '../../../../types/EventInfoType';

import styles from './ThirdPart.module.scss';

type Props = Pick<EventInfoType, 'name' | 'description'>;

export const ThirdPart: React.FC<Props> = ({ name, description }) => {
  const descr = useMemo(() => {
    const dotIndex = [...description].findIndex(ch => ch === '.');
    const firstPart = description.slice(0, dotIndex + 1);
    const secondPart = description.slice(dotIndex + 1);

    return (
      <>
        {firstPart}
        <br />
        {secondPart}
      </>
    );
  }, []);

  return (
    <div className={styles['third-part']}>
      <h3 className={styles.title}>{name}</h3>
      <div className={styles.descr}>{descr}</div>
    </div>
  );
};
