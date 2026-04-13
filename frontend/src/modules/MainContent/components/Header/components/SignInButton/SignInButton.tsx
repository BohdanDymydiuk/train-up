import { FC } from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import { TW_BUTTON_BASE } from '@/constants/tailwind';
import { ButtonProps } from '@/hocs/DropdownHoc';

import clsx from 'clsx';

const classes = {
  // Keep size in CSS variables so related elements stay aligned.
  button: clsx(TW_BUTTON_BASE, 'h-(--signin-height) w-(--signin-width)'),
};

export const SignInButton: FC<ButtonProps> = ({ onClickHandler }) => {
  return (
    <button className={classes.button} onClick={onClickHandler}>
      <div>{AUTH_STRINGS.signInAs}</div>
    </button>
  );
};
