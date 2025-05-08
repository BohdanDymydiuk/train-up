import React from 'react';

import { Form } from './components/Form';
import { Img } from './components/Img';

import styles from './Login.module.scss';

export const Login: React.FC = () => {
  return (
    <div className={styles.login}>
      <Img />
      <Form />
    </div>
  );
};
