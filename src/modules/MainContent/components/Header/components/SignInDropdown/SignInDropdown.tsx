import React from 'react';
import { useNavigate } from 'react-router';

import { APPEARING_DP_CSS_PROPS } from '../../../../../../constants/common';
import { Links } from '../../../../../../enums/Links';
import { DropdownProps } from '../../../../../../reusables/DropdownHoc';

import styles from './SignInDropdown.module.scss';

enum SignIn {
  client = 'Увійти як клієнт',
  trainer = 'Увійти як тренер',
  admin = 'Увійти як адміністратор залу',
}

export const SignInDropdown: React.FC<DropdownProps> = ({ isDpShown }) => {
  const navigate = useNavigate();

  const dpStyle = isDpShown ? APPEARING_DP_CSS_PROPS : {};

  const signInHandler = () => navigate(Links.signIn);

  return (
    <div className={styles.dropdown} style={dpStyle}>
      <ul className={styles['dp-list']}>
        {Object.values(SignIn).map(item => {
          return (
            <li
              className={styles['dp-item']}
              key={item}
              onClick={signInHandler}
            >
              <a className={styles['dp-item-link']}>{item}</a>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
