import React from 'react';

import { LocationSVG } from '../../../../../../reusables/svgs/LocationSVG';
import { useAppSelector } from '../../../../../../store/store';

import styles from './Location.module.scss';

export const Location: React.FC = () => {
  const location = useAppSelector(state => state.location);

  return (
    <div className={styles.location}>
      <LocationSVG />
      <span className={styles.text}>{location}</span>
    </div>
  );
};
