import { FC } from 'react';

import { CalendarSVG } from '@/components/svgs/sectionSvgs/calendar/CalendarSVG';
import { EventsSVG } from '@/components/svgs/sectionSvgs/events/EventsSVG';
import { GymsSVG } from '@/components/svgs/sectionSvgs/GymsSVG';
import { MainSVG } from '@/components/svgs/sectionSvgs/MainSVG';
import { TrainersSVG } from '@/components/svgs/sectionSvgs/trainers/TrainersSVG';
import { NavItems } from '@/shared/enums';

import styles from './Sidebar.module.scss';

export const Sidebar: FC = () => {
  /* eslint-disable react/jsx-key */
  const icons = [
    <MainSVG />,
    <TrainersSVG />,
    <GymsSVG />,
    <EventsSVG />,
    <CalendarSVG />,
  ];
  /* eslint-enable react/jsx-key */

  const items = Object.values(NavItems).filter(
    value => value !== NavItems.aboutUs,
  );

  return (
    <aside className={styles.sidebar}>
      <ul className={styles.list}>
        {items.map((item, index) => {
          return (
            <li key={item} className={styles.item}>
              {icons[index]}
              <div className={styles.title}>{item}</div>
            </li>
          );
        })}
      </ul>
    </aside>
  );
};
