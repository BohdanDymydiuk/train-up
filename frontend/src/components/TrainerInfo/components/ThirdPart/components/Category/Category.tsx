import { FC } from 'react';

interface Props {
  category: string;
}

export const Category: FC<Props> = ({ category }) => {
  return (
    <div className='border-brown-muted flex h-9.75 w-22.5 items-center justify-center rounded-lg border font-[Inter] text-base font-medium'>
      {category}
    </div>
  );
};
