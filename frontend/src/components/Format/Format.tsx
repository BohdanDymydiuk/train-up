import { FC } from 'react';

import { Formats } from '@/shared/enums';

import clsx from 'clsx';

interface Props {
  isOnline: boolean;
  width?: `${number}px`;
  backgroundColor?: 'bg-main';
}

const classes = {
  format: clsx(
    'flex h-8 items-center justify-center rounded-[100px] font-[Inter] text-sm font-medium',
  ),
  offline: clsx('bg-dark text-white'),
  online: clsx('bg-white text-black'),
};

export const Format: FC<Props> = ({
  isOnline,
  width = '106px',
  backgroundColor,
}) => {
  return (
    <div
      style={{ width }}
      className={clsx(classes.format, {
        'bg-main': backgroundColor === 'bg-main',
        [classes.online]: isOnline,
        [classes.offline]: !isOnline,
      })}
    >
      {isOnline ? Formats.online : Formats.offline}
    </div>
  );
};
