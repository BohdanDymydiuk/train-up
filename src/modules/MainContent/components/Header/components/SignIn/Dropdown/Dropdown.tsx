import React from 'react';

import { useNavigate } from 'react-router';

import { NavLinks } from '../../../../../../../enums/NavLinks';

import styles from './Dropdown.module.scss';

enum SignIn {
  client = 'Увійти як клієнт',
  trainer = 'Увійти як тренер',
  admin = 'Увійти як адміністратор залу',
}

export const Dropdown: React.FC = () => {
  const navigate = useNavigate();

  const signInHandler = () => navigate(NavLinks.signIn);

  return (
    <div className={styles.dropdown}>
      <ul className={styles.list}>
        {Object.values(SignIn).map((item, index) => {
          return (
            <li
              className={styles.item}
              key={`singIn-${index}`}
              onClick={signInHandler}
            >
              <div className={styles['item-text']}>{item}</div>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
