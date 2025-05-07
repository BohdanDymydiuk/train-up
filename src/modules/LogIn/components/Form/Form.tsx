import React, { useState } from 'react';

import { useNavigate } from 'react-router';

import { InputChangeEvent } from '../../../../types/Events';

import { Buttons } from './components/Buttons';
import { Inputs } from './components/Inputs';
import { SignUp } from './components/SignUp';
import { Title } from './components/Title';

import styles from './Form.module.scss';

export const Form: React.FC = () => {
  const [emailName, setEmailName] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  // #region handlers

  const inputTextHandler = (event: InputChangeEvent) => {
    setEmailName(event.target.value);
  };

  const inputPasswordHandler = (event: InputChangeEvent) => {
    setPassword(event.target.value);
  };

  const onSubmitHandler = (event: React.FormEvent) => {
    event.preventDefault();

    navigate('/');
  };

  // #endregion

  return (
    <div className={styles['form-wrapper']}>
      <Title />
      <form
        action='#'
        method='post'
        className={styles.form}
        onSubmit={onSubmitHandler}
      >
        <h3 className={styles['form-title']}>
          Welcome to your creative space!
        </h3>
        <Inputs
          emailName={emailName}
          password={password}
          inputTextHandler={inputTextHandler}
          inputPasswordHandler={inputPasswordHandler}
        />
        <Buttons />
      </form>
      <SignUp />
    </div>
  );
};
