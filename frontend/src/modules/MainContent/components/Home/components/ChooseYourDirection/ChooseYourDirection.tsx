import React, { useContext, useState } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';

import { Directions } from './components/Directions';
import { ShowHideMore } from './components/ShowHideMore';

import styles from './ChooseYourDirection.module.scss';

export const ChooseYourDirection: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const [areAllShown, setAreAllShown] = useState(false);

  const ermilovTitleProps = {
    title: 'Оберіть свій напрям',
    CssProps: {
      textAlign: 'center',
      fontSize: onDesktop && '40px',
    } as React.CSSProperties,
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
