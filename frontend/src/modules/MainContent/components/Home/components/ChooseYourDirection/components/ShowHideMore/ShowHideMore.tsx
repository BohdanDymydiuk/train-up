import { Dispatch, FC, SetStateAction } from 'react';

import { DIRECTION_STRINGS } from '@/constants/strings';
import { TW_TYPO_INTER_500_18 } from '@/constants/tailwind';

import clsx from 'clsx';

interface Props {
  areAllShown: boolean;
  setAreAllShown: Dispatch<SetStateAction<boolean>>;
}

const classes = {
  button: clsx(
    TW_TYPO_INTER_500_18,
    'mx-auto mt-10.5 block cursor-pointer border-none bg-transparent md:mt-16.5',
  ),
};

export const ShowHideMore: FC<Props> = ({ areAllShown, setAreAllShown }) => {
  return (
    <button
      className={classes.button}
      onClick={() => setAreAllShown(value => !value)}
    >
      {areAllShown ? DIRECTION_STRINGS.hide : DIRECTION_STRINGS.showAll}
    </button>
  );
};
