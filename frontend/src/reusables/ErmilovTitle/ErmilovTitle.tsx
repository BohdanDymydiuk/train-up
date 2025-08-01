import React, { JSX } from 'react';

import styles from './ErmilovTitle.module.scss';

interface Props {
  title: JSX.Element | string;
  CssProps?: React.CSSProperties;
}

export const ErmilovTitle: React.FC<Props> = ({ title, CssProps }) => {
  return (
    <h2 className={styles.title} style={CssProps}>
      {title}
    </h2>
  );
};
