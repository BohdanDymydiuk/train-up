'use client';

import { FC, FormEvent, useState } from 'react';

import { login } from '@/api/auth';
import { AUTH_STRINGS } from '@/constants/strings';
import { TW_TYPO_ERMILOV_40 } from '@/constants/tailwind';
import { Token } from '@/shared/types/auth';
import { InputChangeEvent } from '@/shared/types/events';
import { useAppDispatch } from '@/store';
import { actions as jwtTokenActions } from '@/store/features/jwtToken';

import clsx from 'clsx';
import { useRouter } from 'next/navigation';

import { Button } from './components/Button';
import { Inputs } from './components/Inputs';
import { SignUp } from './components/SignUp';

const classes = {
  formWrapper: clsx('md:w-163.25'),
  formTitle: clsx(TW_TYPO_ERMILOV_40, 'text-center'),
};

export const Form: FC = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();

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
    router.push('/');

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
    <div className={classes.formWrapper}>
      <form action='#' method='post' onSubmit={onSubmitHandler}>
        <h3 className={classes.formTitle}>{AUTH_STRINGS.welcome}</h3>
        <Inputs {...inputsProps} />
        <Button />
      </form>
      <SignUp />
    </div>
  );
};
