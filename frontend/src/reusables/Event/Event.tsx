import React from 'react';

import { Event as EventType } from '../../types/Event';

import styles from './Event.module.scss';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining'
>;

export const Event: React.FC<Props> = ({
  name,
  description,
  intensity,
  onlineTraining,
}) => {
  return (
    <div className={styles.event}></div>
  );
};
