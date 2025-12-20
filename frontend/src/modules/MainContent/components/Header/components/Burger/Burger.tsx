import { FC } from 'react';

import { BurgerSVG } from '@/components/svgs/headerSvgs/BurgerSVG';

import styles from './Burger.module.scss';

export const Burger: FC = () => {
  return (
    <button className={styles.wrapper}>
      <BurgerSVG />
    </button>
  );
};
