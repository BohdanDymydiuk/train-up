import { FC } from 'react';

import { EventInfoType } from '@/shared/types/events';

type Props = Pick<EventInfoType, 'trainer'>;

const classes = {
  trainer: 'mt-4 flex items-center gap-2',
  avatar: 'bg-ui-gray-500 h-6 w-6 rounded-full',
  text: 'text-15 font-[Inter] font-medium',
};

export const FourthPart: FC<Props> = ({ trainer }) => {
  return (
    <div className={classes.trainer}>
      <div className={classes.avatar} />
      <span className={classes.text}>{trainer}</span>
    </div>
  );
};
