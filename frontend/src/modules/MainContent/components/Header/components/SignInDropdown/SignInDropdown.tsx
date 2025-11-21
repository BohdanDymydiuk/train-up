import React from 'react';
import { useNavigate } from 'react-router';

import { APPEARING_DP_CSS_PROPS } from '@/constants/common';
import { AUTH_STRINGS } from '@/constants/strings';
import { DropdownProps } from '@/reusables/DropdownHoc';
import { Links } from '@/shared/enums';

import styles from './SignInDropdown.module.scss';

const SignInOptions = [
  AUTH_STRINGS.signInAsClient,
  AUTH_STRINGS.signInAsTrainer,
  AUTH_STRINGS.signInAsGymAdmin,
] as const;

export const SignInDropdown: React.FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const navigate = useNavigate();

  const dpStyle = isDpShown ? APPEARING_DP_CSS_PROPS : {};

  const signInHandler = () => {
    navigate(Links.signIn);

    if (closeDpHandler) {
      closeDpHandler();
    }
  };

  return (
    <div className={styles.dropdown} style={dpStyle}>
      <ul className={styles['dp-list']}>
        {SignInOptions.map(item => {
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
