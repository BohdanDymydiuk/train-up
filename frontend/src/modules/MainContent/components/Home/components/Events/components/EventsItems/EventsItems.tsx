import React from 'react';

import { Event } from '../../../../../../../../reusables/Event';
import { useAppSelector } from '../../../../../../../../store/store';

import styles from './EventsItems.module.scss';

export const EventsItems: React.FC = () => {
  const events = useAppSelector(state => state.events);

  return (
    <div className={styles.events}>
      <div className={styles.wrapper}>
        {events.map(event => {
          const { id, name, description, onlineTraining, intensity } = event;

          return (
            <Event
              key={id}
              {...{ name, description, onlineTraining, intensity }}
            />
          );
        })}
      </div>
    </div>
  );
};
