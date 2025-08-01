import React from 'react';

import { Blocks } from './components/Blocks';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  return (
    <section className={styles.events}>
      <Blocks />
    </section>
  );
};
