import { FC } from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { TW_INPUT_BASE, TW_TYPO_INTER_500_15 } from '@/constants/tailwind';

import clsx from 'clsx';

const classes = {
  signin: clsx(
    TW_INPUT_BASE,
    TW_TYPO_INTER_500_15,
    'bg-accent hover:bg-danger focus:bg-danger mt-8 w-full cursor-pointer border-none text-white transition-[background-color] duration-200',
  ),
};

export const Button: FC = () => {
  return (
    <button type='submit' className={classes.signin}>
      {AUTH_STRINGS.signIn}
    </button>
  );
};
