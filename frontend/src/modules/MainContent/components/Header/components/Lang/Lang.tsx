import React, { useEffect, useState } from 'react';

import { ChevronDownSVG } from '@/reusables/svgs/ChevronDownSVG';

import styles from './Lang.module.scss';

export const Lang: React.FC = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <button className={styles.lang}>
      <div>UA</div>
      <ChevronDownSVG />
    </button>
  );
};
