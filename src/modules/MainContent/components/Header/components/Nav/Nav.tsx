import React from 'react';

import styles from './Nav.module.scss';

enum NavItems {
  trainers = 'Тренери',
  gyms = 'Спортзали',
  events = 'Події',
  aboutUs = 'Про нас',
}

export const Nav: React.FC = () => {
  return (
    <nav>
      <ul className={styles.list}>
        {Object.values(NavItems).map((item, index) => {
          return (
            <li key={`nav-${index}`}>
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
