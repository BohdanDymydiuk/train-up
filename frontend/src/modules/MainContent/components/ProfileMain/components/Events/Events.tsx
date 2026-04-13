import { FC } from 'react';

import { EventInfo } from '@/components/EventInfo';
import { LookMore } from '@/components/LookMore';
import { ProfileTitle } from '@/components/ProfileTitle';
import { NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

const classes = {
  events: clsx('mt-7'),
  wrapper: clsx('mt-6 flex flex-wrap gap-x-6 gap-y-8'),
};

export const Events: FC = () => {
  const events = useAppSelector(state => state.events2);

  return (
    <section className={classes.events}>
      <ProfileTitle title={NavItems.events} />
      <div className={classes.wrapper}>
        {events.slice(0, 4).map(event => {
          const { id, ...eventInfoProps } = event;

          return <EventInfo key={id} {...eventInfoProps} />;
        })}
      </div>
      <LookMore />
    </section>
  );
};
