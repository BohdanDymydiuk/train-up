import React from 'react';

import styles from './SignUp.module.scss';

interface Props {
  setIsModalShown: (value: boolean) => void;
}

export const SignUp: React.FC<Props> = ({ setIsModalShown }) => {
  const signupHandler = () => {
    setIsModalShown(true);
  };

  return (
    <div className={styles.signup}>
      Don’t have an account? <strong onClick={signupHandler}>Sign up</strong>
    </div>
  );
};
