import { FC } from 'react';

import { ArrowLeft } from '@/components/svgs/arrows/ArrowLeft';
import { ArrowRight } from '@/components/svgs/arrows/ArrowRight';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

import { ImgIndexProps } from '../../Events';

const classes = {
  arrows: clsx('flex w-full max-w-18.5 justify-between'),
  button: clsx('cursor-pointer border-none bg-transparent'),
};

export const Arrows: FC<ImgIndexProps> = ({ imgIndex, setImgIndex }) => {
  const events = useAppSelector(state => state.events);

  return (
    <div className={classes.arrows}>
      <button
        className={classes.button}
        onClick={() => imgIndex > 0 && setImgIndex(prev => prev - 1)}
      >
        <ArrowLeft />
      </button>

      <button
        className={classes.button}
        onClick={() =>
          imgIndex < events.length - 1 && setImgIndex(prev => prev + 1)
        }
      >
        <ArrowRight />
      </button>
    </div>
  );
};
