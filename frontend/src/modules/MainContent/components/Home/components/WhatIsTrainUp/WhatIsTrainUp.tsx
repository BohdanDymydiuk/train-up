import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';

import { Blocks } from './components/Blocks';

import styles from './WhatIsTrainUp.module.scss';

export const WhatIsTrainUp: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: 'TrainUp — це',
    CssProps: {
      textAlign: 'left',
      fontSize: onDesktop && '40px',
    } as React.CSSProperties,
  };

  return (
    <section className={styles['train-up']}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Blocks />
    </section>
  );
};
