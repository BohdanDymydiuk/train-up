import React, { useContext, useState } from 'react';

import { EVENTS_STRINGS } from '@/constants/strings';
import { MainContext } from '@/context/MainContext';
import { ErmilovTitle } from '@/reusables/ErmilovTitle';

import { Arrows } from './components/Arrows';
import { EventsItems } from './components/EventsItems';

import styles from './Events.module.scss';

export interface ImgIndexProps {
  imgIndex: number;
  setImgIndex: React.Dispatch<React.SetStateAction<number>>;
}

export const Events: React.FC = () => {
  const { onDesktop } = useContext(MainContext);

  const [imgIndex, setImgIndex] = useState(0);

  const ermilovTitleProps = {
    title: EVENTS_STRINGS.upcoming,
    cssProps: {
      fontSize: onDesktop && '40px',
      whiteSpace: 'nowrap',
    } as React.CSSProperties,
  };

  const eventsProps = { imgIndex, setImgIndex };

  return (
    <section className={styles.section}>
      <header className={styles.header}>
        <ErmilovTitle {...ermilovTitleProps} />
        <Arrows {...eventsProps} />
      </header>

      <EventsItems {...eventsProps} />
    </section>
  );
};
