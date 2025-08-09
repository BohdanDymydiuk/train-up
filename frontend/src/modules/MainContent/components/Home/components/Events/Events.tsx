import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';

import { Arrows } from './components/Arrows';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: 'Найближчі події',
    cssProps: {
      fontSize: onDesktop && '40px',
      whiteSpace: 'nowrap',
    } as React.CSSProperties,
  };

  return (
    <section className={styles.section}>
      <header className={styles.header}>
        <ErmilovTitle {...ermilovTitleProps} />
        <Arrows />
      </header>
    </section>
  );
};
