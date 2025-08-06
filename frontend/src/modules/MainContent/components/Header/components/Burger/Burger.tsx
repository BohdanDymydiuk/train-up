import React from 'react';

import { BurgerSVG } from '../../../../../../reusables/svgs/headerSvgs/BurgerSVG';

import styles from './Burger.module.scss';

export const Burger: React.FC = () => {
  return (
    <button className={styles.wrapper}>
      <BurgerSVG />
    </button>
  );
};
