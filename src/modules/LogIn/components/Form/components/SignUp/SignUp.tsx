import React from 'react';

import { useNavigate } from 'react-router';

import { NavLinks } from '../../../../../../enums/NavLinks';

import styles from './SignUp.module.scss';

export const SignUp: React.FC = () => {
  const navigate = useNavigate();

  const signupHandler = () => {
    navigate(NavLinks.signUp);
  };

  return (
    <div className={styles.signup}>
      Don’t have an account? <strong onClick={signupHandler}>Sign up</strong>
    </div>
  );
};
