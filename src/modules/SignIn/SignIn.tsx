import React from 'react';

import { Form } from './components/Form';
import { Img } from './components/Img';

import styles from './SignIn.module.scss';

export const SignIn: React.FC = () => {
  return (
    <div className={styles.signin}>
      <Img />
      <Form />
    </div>
  );
};
