import React from 'react';

import { ChevronDownSVG } from '../../../../../../reusables/svgs/ChevronDownSVG';

import styles from './Lang.module.scss';

export const Lang: React.FC = () => {
  return (
    <button className={styles.lang}>
      <div>UA</div>
      <ChevronDownSVG />
    </button>
  );
};
