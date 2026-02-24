import { FC } from 'react';

import { Formats } from '@/shared/enums';

import clsx from 'clsx';

interface Props {
  type: string;
}

export const TrainingType: FC<Props> = ({ type }) => {
  return (
    <div
      className={clsx(
        'cursor-pointer rounded-lg px-2.5 py-1.25 font-[Inter] text-xs font-medium',
        { 'border-brown-muted border': type === Formats.offline },
        {
          'border border-gray-900 bg-gray-900 text-[#F4DCDC]':
            type === Formats.online,
        },
      )}
    >
      {type}
    </div>
  );
};
