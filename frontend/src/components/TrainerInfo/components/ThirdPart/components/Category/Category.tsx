import { FC } from 'react';

import { TW_TYPO_INTER_500_16 } from '@/constants/tailwind';

import clsx from 'clsx';

interface Props {
  category: string;
}

export const Category: FC<Props> = ({ category }) => {
  return (
    <div
      className={clsx(
        'border-brown-muted flex h-9.75 w-22.5 items-center justify-center rounded-lg border',
        TW_TYPO_INTER_500_16,
      )}
    >
      {category}
    </div>
  );
};
