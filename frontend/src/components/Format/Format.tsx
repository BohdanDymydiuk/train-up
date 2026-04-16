import { FC } from 'react';

import { TW_TYPO_INTER_500_14 } from '@/constants/tailwind';
import { Formats } from '@/shared/enums';

import clsx from 'clsx';

interface Props {
  isOnline: boolean;
  width?: `${number}px`;
  backgroundColor?: 'var(--color-surface-main)';
}

const classes = {
  format: clsx(
    'flex h-8 items-center justify-center rounded-[100px]',
    TW_TYPO_INTER_500_14,
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
      style={{ width, backgroundColor }}
      className={clsx(classes.format, {
        [classes.online]: isOnline,
        [classes.offline]: !isOnline,
      })}
    >
      {isOnline ? Formats.online : Formats.offline}
    </div>
  );
};
