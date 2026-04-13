import { CSSProperties, FC, useContext, useState } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { DIRECTION_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';

import clsx from 'clsx';

import { Directions } from './components/Directions';
import { ShowHideMore } from './components/ShowHideMore';

const classes = {
  section: clsx(
    'mt-8 rounded-2xl bg-white p-4 sm:p-16 md:mt-16 xl:mt-26 xl:p-[64px_75px]',
  ),
};

export const ChooseYourDirection: FC = () => {
  const { onDesktop } = useContext(Context);

  const [areAllShown, setAreAllShown] = useState(false);

  const ermilovTitleProps = {
    title: DIRECTION_STRINGS.title,
    cssProps: {
      textAlign: 'center',
      fontSize: onDesktop && '40px',
    } as CSSProperties,
  };

  const showHideProps = { areAllShown, setAreAllShown };

  return (
    <section className={classes.section}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Directions areAllShown={areAllShown} />
      <ShowHideMore {...showHideProps} />
    </section>
  );
};
