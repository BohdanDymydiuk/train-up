import { FC } from 'react';

import { EventInfo } from '@/components/EventInfo';
import { LookMore } from '@/components/LookMore';
import { ProfileTitle } from '@/components/ProfileTitle';
import { NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import styles from './Events.module.scss';

export const Events: FC = () => {
  const events = useAppSelector(state => state.events2);

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
