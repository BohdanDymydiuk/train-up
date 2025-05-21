import React from 'react';

import { NavItems } from '../../../../../../enums/NavItems';

import styles from './Nav.module.scss';

export const Nav: React.FC = () => {
  const items = Object.values(NavItems).filter(
    value => value !== NavItems.calendar,
  );

  return (
    <nav>
      <ul className={styles.list}>
        {items.map(item => {
          return (
            <li key={item}>
              <a href='#' className={styles.link}>
                {item}
              </a>
            </li>
          );
        })}
      </ul>
    </nav>
  );
};
