import React from 'react';
import { useNavigate } from 'react-router';

import { AUTH_STRINGS } from '@/constants/strings';
import { Links } from '@/shared/enums';

import styles from './SignUp.module.scss';

export const SignUp: React.FC = () => {
  const navigate = useNavigate();

  const signupHandler = () => navigate(Links.signUp);

  return (
    <div className={styles.signup}>
      {AUTH_STRINGS.noAccount}{' '}
      <span className={styles.strong} onClick={signupHandler}>
        {AUTH_STRINGS.signUp}
      </span>
    </div>
  );
};
