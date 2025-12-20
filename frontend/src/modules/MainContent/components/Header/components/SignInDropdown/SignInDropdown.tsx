'use client';

import { FC } from 'react';

import { APPEARING_DP_CSS_PROPS } from '@/constants/common';
import { AUTH_STRINGS } from '@/constants/strings';
import { DropdownProps } from '@/hocs/DropdownHoc';
import { Links } from '@/shared/enums';

import { useRouter } from 'next/navigation';

import styles from './SignInDropdown.module.scss';

const SignInOptions = [
  AUTH_STRINGS.signInAsClient,
  AUTH_STRINGS.signInAsTrainer,
  AUTH_STRINGS.signInAsGymAdmin,
] as const;

export const SignInDropdown: FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const router = useRouter();
  const dpStyle = isDpShown ? APPEARING_DP_CSS_PROPS : {};

  const signInHandler = () => {
    router.push(Links.signIn);

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
