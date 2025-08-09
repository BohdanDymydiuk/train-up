import React, { JSX } from 'react';

import styles from './ErmilovTitle.module.scss';

interface Props {
  title: JSX.Element | string;
  cssProps?: React.CSSProperties;
}

export const ErmilovTitle: React.FC<Props> = ({ title, cssProps }) => {
  return (
    <h2 className={styles.title} style={cssProps}>
      {title}
    </h2>
  );
};
