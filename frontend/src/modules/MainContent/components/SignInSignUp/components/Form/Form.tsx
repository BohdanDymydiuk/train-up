import React, { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router';

import { login } from '@/api/auth';
import { useAppDispatch } from '@/store';
import { actions as jwtTokenActions } from '@/store/features/jwtToken';
import { InputChangeEvent } from '@/types/Events';
import { Token } from '@/types/Token';

import { Button } from './components/Button';
import { Inputs } from './components/Inputs';
import { SignUp } from './components/SignUp';

import styles from './Form.module.scss';

export const Form: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // #region handlers
  const inputTextHandler = (event: InputChangeEvent) => {
    setEmail(event.target.value);
  };

  const inputPasswordHandler = (event: InputChangeEvent) => {
    setPassword(event.target.value);
  };

  const onSubmitHandler = async (event: FormEvent) => {
    event.preventDefault();
    navigate('/');

    const { token }: Token = await login({ email, password });

    dispatch(jwtTokenActions.setToken(token));
  };
  // #endregion

  // #region props
  const inputsProps = {
    email,
    password,
    inputTextHandler,
    inputPasswordHandler,
  };
  // #endregion

  return (
    <div className={styles['form-wrapper']}>
      <form
        action='#'
        method='post'
        className={styles.form}
        onSubmit={onSubmitHandler}
      >
        <h3 className={styles['form-title']}>Вітаємо у TrainUp</h3>
        <Inputs {...inputsProps} />
        <Button />
      </form>
      <SignUp />
    </div>
  );
};
