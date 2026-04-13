import { FC } from 'react';

import { LocationSVG } from '@/components/svgs/LocationSVG';
import { TW_TYPO_INTER_500_14 } from '@/constants/tailwind';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

const classes = {
  location: clsx(
    'bg-dark absolute top-0 right-0 flex h-10 w-fit items-center justify-center gap-2 rounded-lg px-3.5',
  ),
  text: clsx(TW_TYPO_INTER_500_14, 'text-white'),
};

export const Location: FC = () => {
  const location = useAppSelector(state => state.location);

  return (
    <div className={classes.location}>
      <LocationSVG />
      <span className={classes.text}>{location}</span>
    </div>
  );
};
