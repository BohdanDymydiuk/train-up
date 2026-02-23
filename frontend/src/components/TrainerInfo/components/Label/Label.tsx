import { FC } from 'react';

import { ALT_STRINGS, TRAINER_STRINGS } from '@/constants/strings';

import Image from 'next/image';

const classes = {
  label:
    'border-brown-muted absolute -top-6.25 left-6 flex gap-2 rounded-lg border bg-white px-2 py-2.5',
  celebration: 'h-4.25',
  text: 'font-[Inter] text-sm font-medium',
};

export const Label: FC = () => {
  return (
    <div className={classes.label}>
      <Image
        className={classes.celebration}
        src='/images/celebration.png'
        alt={ALT_STRINGS.celebration}
      />
      <span className={classes.text}>{TRAINER_STRINGS.newTrainer}</span>
    </div>
  );
};
