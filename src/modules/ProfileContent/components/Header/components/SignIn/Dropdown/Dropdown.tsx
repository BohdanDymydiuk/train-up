import React from 'react';
import { useNavigate } from 'react-router';

import { NavLinks } from '../../../../../../../enums/NavLinks';

import styles from './Dropdown.module.scss';

interface Props {
  isDpShown: boolean;
}

enum SignIn {
  client = 'Увійти як клієнт',
  trainer = 'Увійти як тренер',
  admin = 'Увійти як адміністратор залу',
}

export const Dropdown: React.FC<Props> = ({ isDpShown }) => {
  const navigate = useNavigate();

  const dpCssProps: React.CSSProperties = {
    opacity: 1,
    transform: 'scaleY(1)',
  };

  const dpStyle = isDpShown ? dpCssProps : {};

  const signInHandler = () => navigate(NavLinks.signIn);

  return (
    <div className={styles.dropdown} style={dpStyle}>
      <ul className={styles.list}>
        {Object.values(SignIn).map(item => {
          return (
            <li className={styles.item} key={item} onClick={signInHandler}>
              <div className={styles['item-text']}>{item}</div>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
