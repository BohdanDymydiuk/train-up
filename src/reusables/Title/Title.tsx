import React from 'react';

import styles from './Title.module.scss';

interface Props {
  title: string;
}

export const Title: React.FC<Props> = ({ title }) => {
  return <h2 className={styles.title}>{title}</h2>;
};
