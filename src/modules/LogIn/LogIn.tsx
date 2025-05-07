import React from 'react';

import { Form } from './components/Form';
import { Img } from './components/Img';

import styles from './LogIn.module.scss';

export const LogIn: React.FC = () => {
  return (
    <div className={styles.login}>
      <Img />
      <Form />
    </div>
  );
};
