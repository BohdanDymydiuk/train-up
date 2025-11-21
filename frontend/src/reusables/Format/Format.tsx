import React from 'react';

import { Formats } from '@/shared/enums';

import clsx from 'clsx';

import styles from './Format.module.scss';

interface Props {
  isOnline: boolean;
  width?: `${number}px`;
  backgroundColor?: string;
}

export const Format: React.FC<Props> = ({
  isOnline,
  width = '106px',
  backgroundColor,
}) => {
  return (
    <div
      style={{ width, backgroundColor }}
      className={clsx(styles.format, {
        [styles.online]: isOnline,
        [styles.offline]: !isOnline,
      })}
    >
      {isOnline ? Formats.online : Formats.offline}
    </div>
  );
};
