import React from 'react';

import clsx from 'clsx';

import { Formats } from '../../enums/Formats';

import styles from './Format.module.scss';

interface Props {
  isOnline: boolean;
  width?: `${number}px`;
}

export const Format: React.FC<Props> = ({ isOnline, width = '106px' }) => {
  return (
    <div
      style={{ width }}
      className={clsx(styles.format, {
        [styles.online]: isOnline,
        [styles.offline]: !isOnline,
      })}
    >
      {isOnline ? Formats.online : Formats.offline}
    </div>
  );
};
