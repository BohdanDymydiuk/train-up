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

  const dpCSSProps: React.CSSProperties = {
    opacity: 1,
    transform: 'scaleY(1)',
  };

  const dpStyles = isDpShown ? dpCSSProps : {};

  const signInHandler = () => navigate(NavLinks.signIn);

  return (
    <div className={styles.dropdown} style={dpStyles}>
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
