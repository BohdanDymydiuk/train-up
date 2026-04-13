import { FC, useEffect, useState } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';
import { TW_TYPO_INTER_500_18 } from '@/constants/tailwind';

import clsx from 'clsx';

const classes = {
  button: clsx(
    'text-dark flex items-center gap-2 border-none bg-transparent',
    TW_TYPO_INTER_500_18,
  ),
};

export const Lang: FC = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <button className={classes.button}>
      <div>UA</div>
      <ChevronDownSVG />
    </button>
  );
};
