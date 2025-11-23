'use client';

import React from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { Links } from '@/shared/enums';

import { useRouter } from 'next/navigation';

import styles from './SignUp.module.scss';

export const SignUp: React.FC = () => {
  const router = useRouter();

  const signupHandler = () => router.push(Links.signUp);

  return (
    <div className={styles.signup}>
      {AUTH_STRINGS.noAccount}{' '}
      <span className={styles.strong} onClick={signupHandler}>
        {AUTH_STRINGS.signUp}
      </span>
    </div>
  );
};
