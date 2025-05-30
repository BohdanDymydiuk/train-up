import React from 'react';

import { Finder } from './components/Finder';

import styles from './SportFinder.module.scss';

export const SportFinder: React.FC = () => {
  return (
    <section className={styles['sport-finder']}>
      <Finder />
    </section>
  );
};
