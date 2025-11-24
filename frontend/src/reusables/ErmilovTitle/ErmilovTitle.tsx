import React, { JSX, useEffect, useState } from 'react';

import styles from './ErmilovTitle.module.scss';

interface Props {
  title: JSX.Element | string;
  cssProps?: React.CSSProperties;
}

export const ErmilovTitle: React.FC<Props> = ({ title, cssProps }) => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <h2 className={styles.title} style={cssProps}>
      {title}
    </h2>
  );
};
