import { CSSProperties, FC, useContext, useState } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { DIRECTION_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';

import { Directions } from './components/Directions';
import { ShowHideMore } from './components/ShowHideMore';

import styles from './ChooseYourDirection.module.scss';

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
    <section className={styles.section}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Directions areAllShown={areAllShown} />
      <ShowHideMore {...showHideProps} />
    </section>
  );
};
