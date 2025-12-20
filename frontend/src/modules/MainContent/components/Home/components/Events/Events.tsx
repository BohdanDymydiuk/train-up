import {
  CSSProperties,
  Dispatch,
  FC,
  SetStateAction,
  useContext,
  useState,
} from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { EVENTS_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';

import { Arrows } from './components/Arrows';

import styles from './Events.module.scss';

export interface ImgIndexProps {
  imgIndex: number;
  setImgIndex: Dispatch<SetStateAction<number>>;
}

export const Events: FC = () => {
  const { onDesktop } = useContext(Context);

  const [imgIndex, setImgIndex] = useState(0);

  const ermilovTitleProps = {
    title: EVENTS_STRINGS.upcoming,
    cssProps: {
      fontSize: onDesktop && '40px',
      whiteSpace: 'nowrap',
    } as CSSProperties,
  };

  const eventsProps = { imgIndex, setImgIndex };

  return (
    <section className={styles.section}>
      <header className={styles.header}>
        <ErmilovTitle {...ermilovTitleProps} />
        <Arrows {...eventsProps} />
      </header>

      {/* <EventsItems {...eventsProps} /> */}
    </section>
  );
};
