import { FC } from 'react';

import { TW_TYPO_INTER_600_12 } from '@/constants/tailwind';

import clsx from 'clsx';

export const LookMore: FC = () => {
  return (
    <div className={clsx('mt-3.75 text-center', TW_TYPO_INTER_600_12)}>
      Дивитись більше
    </div>
  );
};
