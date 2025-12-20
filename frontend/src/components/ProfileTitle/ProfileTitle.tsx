import { FC } from 'react';

import { NavItems } from '@/shared/enums';

import styles from './ProfileTitle.module.scss';

interface Props {
  title: NavItems;
}

export const ProfileTitle: FC<Props> = ({ title }) => {
  return <h2 className={styles.title}>{title}</h2>;
};
