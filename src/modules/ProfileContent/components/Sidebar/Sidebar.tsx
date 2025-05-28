import React from 'react';

import { NavItems } from '../../../../enums/NavItems';
import { CalendarSVG } from '../../../../reusables/svgs/CalendarSVG';
import { EventsSVG } from '../../../../reusables/svgs/EventsSVG';
import { GymsSVG } from '../../../../reusables/svgs/GymsSVG';
import { MainSVG } from '../../../../reusables/svgs/MainSVG';
import { TrainersSVG } from '../../../../reusables/svgs/TrainersSVG';

import styles from './Sidebar.module.scss';

export const Sidebar: React.FC = () => {
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
