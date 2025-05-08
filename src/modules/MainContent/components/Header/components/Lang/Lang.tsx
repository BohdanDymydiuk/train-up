import React from 'react';

import { ChevronDown } from '../../../../../../reusables/ChevronDown';

import styles from './Lang.module.scss';

export const Lang: React.FC = () => {
  return (
    <button className={styles.lang}>
      <div>UA</div>
      <ChevronDown />
    </button>
  );
};
