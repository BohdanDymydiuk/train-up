import React from 'react';

import { Title } from '../../../../../../reusables/Title';
import { ViewAll } from '../../../../../../reusables/ViewAll';

import { Blocks } from './components/Blocks';

import styles from './Events.module.scss';

export const Events: React.FC = () => {
  return (
    <section className={styles.events}>
      <Title title={'Найближчі події'} />
      <Blocks />
      <ViewAll text={'Переглянути всі категорії'} />
    </section>
  );
};
