import { FC } from 'react';

import { NavItems } from '@/shared/enums';

interface Props {
  title: NavItems;
}

export const ProfileTitle: FC<Props> = ({ title }) => {
  return <h2 className='font-[Inter] text-2xl font-semibold'>{title}</h2>;
};
