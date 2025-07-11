import React from 'react';

import { NavItems } from '../../../../../../enums/NavItems';
import { useAppSelector } from '../../../../../../redux/store';
import { EventInfo } from '../../../../../../reusables/EventInfo';
import { LookMore } from '../../../../../../reusables/LookMore';
import { ProfileTitle } from '../../../../../../reusables/ProfileTitle';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  const events = useAppSelector(state => state.events);

  return (
    <section className={styles.events}>
      <ProfileTitle title={NavItems.events} />
      <div className={styles.wrapper}>
        {events.slice(0, 4).map(event => {
          const { id, ...eventInfoProps } = event;

          return <EventInfo key={id} {...eventInfoProps} />;
        })}
      </div>
      <LookMore />
    </section>
  );
};
