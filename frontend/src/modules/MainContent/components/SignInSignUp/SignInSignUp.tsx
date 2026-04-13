import { FC } from 'react';

import clsx from 'clsx';

import { Form } from './components/Form';

const classes = {
  signinSignup: clsx(
    'mx-auto mt-29.5 mb-1.5 max-w-176 rounded-3xl bg-white px-4 py-12.5',
    'md:mb-18.25 md:flex md:max-w-220.5 md:justify-center md:py-19',
    'xl:mb-26',
  ),
};

export const SignInSignUp: FC = () => {
  return (
    <div className={classes.signinSignup}>
      <Form />
    </div>
  );
};
