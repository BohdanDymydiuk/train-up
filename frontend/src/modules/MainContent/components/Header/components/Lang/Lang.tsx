import { FC, useEffect, useState } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';

import styles from './Lang.module.scss';

export const Lang: FC = () => {
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
