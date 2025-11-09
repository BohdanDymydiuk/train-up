import React from 'react';

import { Trainer } from '@/shared/types/trainer';

import { Category } from './components/Category';

import styles from './ThirdPart.module.scss';

type Props = Pick<Trainer, 'categories'>;

export const ThirdPart: React.FC<Props> = ({ categories }) => {
  return (
    <div className={styles['third-part']}>
      {categories.map(category => {
        const categoryProps = { category };

        return <Category key={category} {...categoryProps} />;
      })}
    </div>
  );
};
