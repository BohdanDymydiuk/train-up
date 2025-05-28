import React from 'react';

import { Title } from '../../../../../../reusables/Title';
import { ViewAll } from '../../../../../../reusables/ViewAll';

import { Blocks } from './components/Blocks';

import styles from './Categories.module.scss';

export const Categories: React.FC = () => {
  return (
    <section className={styles.categories}>
      <Title title={'Обери свій напрям'} />
      <Blocks />
      <ViewAll text={'Переглянути всі категорії'} />
    </section>
  );
};
