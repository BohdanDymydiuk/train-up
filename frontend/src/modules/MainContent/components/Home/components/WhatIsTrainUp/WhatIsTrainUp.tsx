import { CSSProperties, FC, useContext } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { ABOUT_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';

import { Blocks } from './components/Blocks';

export const WhatIsTrainUp: FC = () => {
  const { onDesktop } = useContext(Context);

  const ermilovTitleProps = {
    title: ABOUT_STRINGS.title,
    cssProps: { fontSize: onDesktop && '40px' } as CSSProperties,
  };

  return (
    <section className='pt-8.5 md:pt-16'>
      <ErmilovTitle {...ermilovTitleProps} />
      <Blocks />
    </section>
  );
};
