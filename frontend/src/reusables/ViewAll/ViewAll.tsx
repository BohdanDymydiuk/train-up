import React from 'react';

import styles from './ViewAll.module.scss';

interface Props {
  text: string;
}

export const ViewAll: React.FC<Props> = ({ text }) => {
  return <div className={styles.text}>{text}</div>;
};
