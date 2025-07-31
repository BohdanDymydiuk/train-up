import React from 'react';

import { ViewAll } from '../../../../../../reusables/ViewAll';

import { Blocks } from './components/Blocks';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  return (
    <section className={styles.events}>
      <Blocks />
      <ViewAll text={'Переглянути всі категорії'} />
    </section>
  );
};
