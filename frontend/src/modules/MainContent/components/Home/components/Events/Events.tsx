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

import clsx from 'clsx';

import { Arrows } from './components/Arrows';

export interface ImgIndexProps {
  imgIndex: number;
  setImgIndex: Dispatch<SetStateAction<number>>;
}

const classes = {
  section: clsx('mt-8 md:mt-16 xl:mt-18'),
  header: clsx('flex justify-between gap-5'),
};

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
    <section className={classes.section}>
      <header className={classes.header}>
        <ErmilovTitle {...ermilovTitleProps} />
        <Arrows {...eventsProps} />
      </header>

      {/* <EventsItems {...eventsProps} /> */}
    </section>
  );
};
