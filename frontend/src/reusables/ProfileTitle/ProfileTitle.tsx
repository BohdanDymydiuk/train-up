import React from 'react';

import { NavItems } from '../../enums/NavItems';

import styles from './ProfileTitle.module.scss';

interface Props {
  title: NavItems;
}

export const ProfileTitle: React.FC<Props> = ({ title }) => {
  return <h2 className={styles.title}>{title}</h2>;
};
