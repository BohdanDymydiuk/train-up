import React from 'react';

import { InputChangeEvent } from '../../../../../../../../types/Events';

import styles from './Inputs.module.scss';

interface Props {
  emailName: string;
  password: string;
  inputTextHandler: (event: InputChangeEvent) => void;
  inputPasswordHandler: (event: InputChangeEvent) => void;
}

export const Inputs: React.FC<Props> = props => {
  const { emailName, password, inputTextHandler, inputPasswordHandler } = props;

  return (
    <>
      <input
        type='text'
        name='email-name'
        id='email-name'
        value={emailName}
        className={styles.input}
        onChange={inputTextHandler}
        placeholder='Введіть e-mail'
        required
      />
      <input
        type='password'
        name='password'
        id='password'
        value={password}
        className={styles.input}
        onChange={inputPasswordHandler}
        placeholder='Пароль'
        required
      />
    </>
  );
};
