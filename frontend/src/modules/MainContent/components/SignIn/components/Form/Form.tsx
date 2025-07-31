import React, { FormEvent, useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import { useNavigate } from 'react-router';

import { login } from '../../../../../../api/auth';
import { actions as jwtTokenActions } from '../../../../../../store/features/jwtToken';
import { useAppDispatch } from '../../../../../../store/store';
import { InputChangeEvent } from '../../../../../../types/Events';
import { Token } from '../../../../../../types/Token';

import { Button } from './components/Button';
import { Inputs } from './components/Inputs';
import { SignUp } from './components/SignUp';
import { SignUpModal } from './components/SignUpModal';

import styles from './Form.module.scss';

const body = document.body;
const root = document.querySelector('#root') as HTMLElement;

export const Form: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [isModalShown, setIsModalShown] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // #region useEffects
  useEffect(() => {
    if (isModalShown) {
      body.style.overflow = 'hidden';
      root.style.overflowY = 'scroll';
    } else {
      body.style.overflow = 'auto';
      root.style.overflowY = 'auto';
    }
  }, [isModalShown]);
  // #endregion

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

  const signUpProps = { setIsModalShown };
  const modalProps = { setIsModalShown };
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
      {isModalShown &&
        createPortal(<SignUpModal {...modalProps} />, document.body)}
      <SignUp {...signUpProps} />
    </div>
  );
};
