'use client';

import { FC } from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import {
  TW_TYPO_INTER_400_16,
  TW_TYPO_INTER_500_16,
} from '@/constants/tailwind';
import { Links } from '@/shared/enums';

import clsx from 'clsx';
import { useRouter } from 'next/navigation';

const classes = {
  signUp: clsx(TW_TYPO_INTER_400_16, 'text-dark mt-8 text-center'),
  strong: clsx(TW_TYPO_INTER_500_16, 'cursor-pointer'),
};

export const SignUp: FC = () => {
  const router = useRouter();

  const signupHandler = () => router.push(Links.signUp);

  return (
    <div className={classes.signUp}>
      {AUTH_STRINGS.noAccount}{' '}
      <span className={classes.strong} onClick={signupHandler}>
        {AUTH_STRINGS.signUp}
      </span>
    </div>
  );
};
