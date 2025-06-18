import React from 'react';

import { NavItems } from '../../../../../../../../enums/NavItems';

import styles from './NavElems.module.scss';

interface Props {
  currentSection?: string;
}

export const NavElems: React.FC<Props> = ({ currentSection }) => {
  const items = Object.values(NavItems).filter(
    value => value !== NavItems.calendar,
  );

  return (
    <ul className={styles.list}>
      {items.map(item => {
        if (item === currentSection) {
          return;
        }

        return (
          <li key={item}>
            <a href='#' className={styles.link}>
              {item}
            </a>
          </li>
        );
      })}
    </ul>
  );
};
