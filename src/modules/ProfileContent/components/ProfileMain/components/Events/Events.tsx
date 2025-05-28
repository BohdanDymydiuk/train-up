import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { NavItems } from '../../../../../../enums/NavItems';
import { EventInfo } from '../../../../../../reusables/EventInfo';
import { LookMore } from '../../../../../../reusables/LookMore';
import { ProfileTitle } from '../../../../../../reusables/ProfileTitle';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  const { events } = useContext(MainContext);

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
