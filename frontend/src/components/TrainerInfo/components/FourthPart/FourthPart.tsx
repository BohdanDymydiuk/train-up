import { FC } from 'react';

import { StarSVG } from '@/components/svgs/sectionSvgs/trainers/StarSVG';
import { Trainer } from '@/shared/types/trainer';

import clsx from 'clsx';

type Props = Pick<Trainer, 'reviews'>;

const classes = {
  root: clsx('flex flex-col items-end gap-4'),
  reviews: clsx('flex gap-2'),
  number: clsx('font-[Inter] text-sm font-normal'),
  signup: clsx(
    'flex h-12 w-44.5 items-center justify-center rounded-lg border-none bg-gray-300 font-[Inter] text-base font-medium text-white',
  ),
};

export const FourthPart: FC<Props> = ({ reviews }) => {
  return (
    <div className={classes.root}>
      <div className={classes.reviews}>
        <span className={classes.number}>{reviews}</span>
        <StarSVG />
      </div>
      <button className={classes.signup}>Записатись</button>
    </div>
  );
};
