import { FC } from 'react';

import { EventInfoType } from '@/shared/types/events';

import clsx from 'clsx';

type Props = Pick<EventInfoType, 'trainer'>;

const classes = {
  trainer: clsx('mt-4 flex items-center gap-2'),
  avatar: clsx('bg-ui-gray-500 h-6 w-6 rounded-full'),
  text: clsx('text-15 font-[Inter] font-medium'),
};

export const FourthPart: FC<Props> = ({ trainer }) => {
  return (
    <div className={classes.trainer}>
      <div className={classes.avatar} />
      <span className={classes.text}>{trainer}</span>
    </div>
  );
};
