import React from 'react';

import { Form } from './components/Form';

import styles from './SignInSignUp.module.scss';

export const SignInSignUp: React.FC = () => {
  return (
    <div className={styles['signin-signup']}>
      <Form />
    </div>
  );
};
