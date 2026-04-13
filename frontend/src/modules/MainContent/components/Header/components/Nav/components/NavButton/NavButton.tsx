import { FC } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';
import { TW_TYPO_INTER_400_16 } from '@/constants/tailwind';
import { ButtonProps } from '@/hocs/DropdownHoc';

import clsx from 'clsx';

const classes = {
  button: clsx(
    'flex h-6 w-25 cursor-pointer items-center justify-between border-none bg-transparent',
    TW_TYPO_INTER_400_16,
  ),
};

export const NavButton: FC<ButtonProps> = ({ onClickHandler, isDpShown }) => {
  const chevronDownSvgProps = isDpShown ? { fill: 'var(--color-accent)' } : {};

  return (
    <button
      className={classes.button}
      onClick={onClickHandler}
    >
      <div>Головна</div>
      <ChevronDownSVG {...chevronDownSvgProps} />
    </button>
  );
};
