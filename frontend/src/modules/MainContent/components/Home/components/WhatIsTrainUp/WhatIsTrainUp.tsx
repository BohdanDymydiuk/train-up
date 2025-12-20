import { CSSProperties, FC, useContext } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { ABOUT_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';

import { Blocks } from './components/Blocks';

import styles from './WhatIsTrainUp.module.scss';

export const WhatIsTrainUp: FC = () => {
  const { onDesktop } = useContext(Context);

  const ermilovTitleProps = {
    title: ABOUT_STRINGS.title,
    cssProps: { fontSize: onDesktop && '40px' } as CSSProperties,
  };

  return (
    <section className={styles['train-up']}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Blocks />
    </section>
  );
};
