import React, { useContext } from 'react';

import { ABOUT_STRINGS } from '@/constants/strings';
import { MainContext } from '@/providers/MainContext';
import { ErmilovTitle } from '@/reusables/ErmilovTitle';

import { Blocks } from './components/Blocks';

import styles from './WhatIsTrainUp.module.scss';

export const WhatIsTrainUp: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: ABOUT_STRINGS.title,
    cssProps: { fontSize: onDesktop && '40px' } as React.CSSProperties,
  };

  return (
    <section className={styles['train-up']}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Blocks />
    </section>
  );
};
