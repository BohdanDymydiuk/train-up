import React from 'react';

import { ViewAll } from '../../../../../../reusables/ViewAll';

import { Blocks } from './components/Blocks';

import styles from './Categories.module.scss';

export const Categories: React.FC = () => {
  return (
    <section className={styles.categories}>
      <Blocks />
      <ViewAll text={'Переглянути всі категорії'} />
    </section>
  );
};
