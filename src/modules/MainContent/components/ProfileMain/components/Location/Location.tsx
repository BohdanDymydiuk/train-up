import React from 'react';

import { useAppSelector } from '../../../../../../redux/store';
import { LocationSVG } from '../../../../../../reusables/svgs/LocationSVG';

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
