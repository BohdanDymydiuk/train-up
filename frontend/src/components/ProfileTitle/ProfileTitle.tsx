import { FC } from 'react';

import { TW_TYPO_INTER_600_24 } from '@/constants/tailwind';
import { NavItems } from '@/shared/enums';

interface Props {
  title: NavItems;
}

export const ProfileTitle: FC<Props> = ({ title }) => {
  return <h2 className={TW_TYPO_INTER_600_24}>{title}</h2>;
};
