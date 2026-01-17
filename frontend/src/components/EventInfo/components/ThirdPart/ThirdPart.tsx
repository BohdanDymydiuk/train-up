import { FC, useMemo } from 'react';

import { EventInfoType } from '@/shared/types/events';

import styles from './ThirdPart.module.css';

type Props = Pick<EventInfoType, 'name' | 'description'>;

export const ThirdPart: FC<Props> = ({ name, description }) => {
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
