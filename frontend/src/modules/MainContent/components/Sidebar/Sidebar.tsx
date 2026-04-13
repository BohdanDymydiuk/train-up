import { FC } from 'react';

import { CalendarSVG } from '@/components/svgs/sectionSvgs/calendar/CalendarSVG';
import { EventsSVG } from '@/components/svgs/sectionSvgs/events/EventsSVG';
import { GymsSVG } from '@/components/svgs/sectionSvgs/GymsSVG';
import { MainSVG } from '@/components/svgs/sectionSvgs/MainSVG';
import { TrainersSVG } from '@/components/svgs/sectionSvgs/trainers/TrainersSVG';
import { TW_TYPO_INTER_600_12 } from '@/constants/tailwind';
import { NavItems } from '@/shared/enums';

import clsx from 'clsx';

const classes = {
  sidebar: clsx('min-w-31.75'),
  list: clsx('sticky inset-0 flex list-none flex-col gap-6 pt-6'),
  item: clsx(
    TW_TYPO_INTER_600_12,
    'flex cursor-pointer flex-col items-center justify-center gap-2 text-gray-500 hover:text-gray-700',
    '[&_svg_path]:transition-[fill] [&_svg_path]:duration-200 hover:[&_svg_path]:fill-gray-700',
  ),
  title: clsx('transition-[color] duration-200'),
};

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
    <aside className={classes.sidebar}>
      <ul className={classes.list}>
        {items.map((item, index) => {
          return (
            <li key={item} className={classes.item}>
              {icons[index]}
              <div className={classes.title}>{item}</div>
            </li>
          );
        })}
      </ul>
    </aside>
  );
};
