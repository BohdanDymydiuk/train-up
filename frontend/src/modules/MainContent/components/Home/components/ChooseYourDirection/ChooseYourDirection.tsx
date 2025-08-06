import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';

import { Directions } from './components/Directions';

import styles from './ChooseYourDirection.module.scss';

export const ChooseYourDirection: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: 'Оберіть свій напрям',
    CssProps: {
      textAlign: 'center',
      fontSize: onDesktop && '40px',
    } as React.CSSProperties,
  };

  return (
    <section className={styles.section}>
      <ErmilovTitle {...ermilovTitleProps} />
      <Directions />
    </section>
  );
};
