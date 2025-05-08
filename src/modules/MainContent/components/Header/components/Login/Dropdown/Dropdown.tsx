import React from 'react';

import { useNavigate } from 'react-router';

import styles from './Dropdown.module.scss';

enum Login {
  client = 'Увійти як клієнт',
  trainer = 'Увійти як тренер',
  admin = 'Увійти як адміністратор залу',
}

export const Dropdown: React.FC = () => {
  const navigate = useNavigate();

  const loginHandler = () => navigate('/login');

  return (
    <div className={styles.dropdown}>
      <ul className={styles.list}>
        {Object.values(Login).map((item, index) => {
          return (
            <li
              className={styles.item}
              key={`singIn-${index}`}
              onClick={loginHandler}
            >
              <div className={styles['item-text']}>{item}</div>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
