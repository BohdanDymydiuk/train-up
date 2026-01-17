import { FC } from 'react';

import { Formats } from '@/shared/enums';

import clsx from 'clsx';

import styles from './Format.module.css';

interface Props {
  isOnline: boolean;
  width?: `${number}px`;
  backgroundColor?: 'bg-main';
}

export const Format: FC<Props> = ({
  isOnline,
  width = '106px',
  backgroundColor,
}) => {
  return (
    <div
      style={{ width }}
      className={clsx(styles.format, {
        'bg-main': backgroundColor === 'bg-main',
        [styles.online]: isOnline,
        [styles.offline]: !isOnline,
      })}
    >
      {isOnline ? Formats.online : Formats.offline}
    </div>
  );
};
