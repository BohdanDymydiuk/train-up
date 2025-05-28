import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { LocationSVG } from '../../../../../../reusables/svgs/LocationSVG';

import styles from './Location.module.scss';

export const Location: React.FC = () => {
  const { location } = useContext(MainContext);

  return (
    <div className={styles.location}>
      <LocationSVG />
      <span className={styles.text}>{location}</span>
    </div>
  );
};
