import React from 'react';

import { ArrowLeft } from '../../../../../../../../reusables/svgs/arrows/ArrowLeft';
import { ArrowRight } from '../../../../../../../../reusables/svgs/arrows/ArrowRight';

import styles from './Arrows.module.scss';

export const Arrows: React.FC = () => {
  const arrows = {
    left: <ArrowLeft />,
    rigth: <ArrowRight />,
  };

  const keys = Object.keys(arrows);
  const values = Object.values(arrows);

  return (
    <div className={styles.arrows}>
      {keys.map((key, index) => {
        const svg = values[index];

        return (
          <button className={styles.button} key={key}>
            {svg}
          </button>
        );
      })}
    </div>
  );
};
