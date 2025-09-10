import React from 'react';

import { motion } from 'motion/react';

import { Event } from '../../../../../../../../reusables/Event';
import { useAppSelector } from '../../../../../../../../store/store';

import styles from './EventsItems.module.scss';

export const EventsItems: React.FC = () => {
  // const { eventWidth } = useContext(MainContext);

  const events = useAppSelector(state => state.events);

  return (
    <div className={styles.events}>
      <motion.div
        drag='x'
        dragConstraints={{
          right: 0,
          left: 0,
        }}
        className={styles.wrapper}
      >
        {events.map(event => {
          const { id, name, description, onlineTraining, intensity } = event;

          return (
            <Event
              key={id}
              {...{
                name,
                description,
                onlineTraining,
                intensity,
              }}
            />
          );
        })}
      </motion.div>
    </div>
  );
};
