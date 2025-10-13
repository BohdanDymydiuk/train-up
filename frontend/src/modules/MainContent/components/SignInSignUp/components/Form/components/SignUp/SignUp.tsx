import React from 'react';
import { useNavigate } from 'react-router';

import { Links } from '@/enums/Links';

import styles from './SignUp.module.scss';

export const SignUp: React.FC = () => {
  const navigate = useNavigate();

  const signupHandler = () => navigate(Links.signUp);

  return (
    <div className={styles.signup}>
      Досі немає акаунта?{' '}
      <span className={styles.strong} onClick={signupHandler}>
        Зареєструватись
      </span>
    </div>
  );
};
