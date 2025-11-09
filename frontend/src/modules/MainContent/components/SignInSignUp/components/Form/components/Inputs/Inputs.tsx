import React from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { InputChangeEvent } from '@/types/Events';

import styles from './Inputs.module.scss';

interface Props {
  email: string;
  password: string;
  inputTextHandler: (event: InputChangeEvent) => void;
  inputPasswordHandler: (event: InputChangeEvent) => void;
}

export const Inputs: React.FC<Props> = props => {
  const { email, password, inputTextHandler, inputPasswordHandler } = props;

  return (
    <>
      <input
        type='text'
        name='email-name'
        id='email-name'
        value={email}
        className={styles.input}
        onChange={inputTextHandler}
        placeholder={AUTH_STRINGS.emailPlaceholder}
        required
      />
      <input
        type='password'
        name='password'
        id='password'
        value={password}
        className={styles.input}
        onChange={inputPasswordHandler}
        placeholder={AUTH_STRINGS.passwordPlaceholder}
        required
      />
    </>
  );
};
