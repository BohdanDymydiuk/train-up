import { FC } from 'react';

import { LocationSVG } from '@/components/svgs/LocationSVG';
import { useAppSelector } from '@/store';

import styles from './Location.module.scss';

export const Location: FC = () => {
  const location = useAppSelector(state => state.location);

  return (
    <div className={styles.location}>
      <LocationSVG />
      <span className={styles.text}>{location}</span>
    </div>
  );
};
