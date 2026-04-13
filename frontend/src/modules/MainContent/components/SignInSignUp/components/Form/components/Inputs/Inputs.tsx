import { FC } from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { TW_INPUT_BASE, TW_TYPO_INTER_500_15 } from '@/constants/tailwind';
import { InputChangeEvent } from '@/shared/types/events';

import clsx from 'clsx';

const classes = {
  input: clsx(
    TW_INPUT_BASE,
    TW_TYPO_INTER_500_15,
    'w-full border border-gray-100 placeholder:text-gray-200 first-of-type:mt-20 nth-of-type-2:mt-4',
  ),
};

interface Props {
  email: string;
  password: string;
  inputTextHandler: (event: InputChangeEvent) => void;
  inputPasswordHandler: (event: InputChangeEvent) => void;
}

export const Inputs: FC<Props> = props => {
  const { email, password, inputTextHandler, inputPasswordHandler } = props;

  return (
    <>
      <input
        type='text'
        name='email-name'
        id='email-name'
        value={email}
        className={classes.input}
        onChange={inputTextHandler}
        placeholder={AUTH_STRINGS.emailPlaceholder}
        required
      />
      <input
        type='password'
        name='password'
        id='password'
        value={password}
        className={classes.input}
        onChange={inputPasswordHandler}
        placeholder={AUTH_STRINGS.passwordPlaceholder}
        required
      />
    </>
  );
};
