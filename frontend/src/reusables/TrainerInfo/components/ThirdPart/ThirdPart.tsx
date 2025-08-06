import React from 'react';

import { TrainerInfoType } from '../../../../types/TrainerInfoType';

import { Category } from './components/Category';

import styles from './ThirdPart.module.scss';

type Props = Pick<TrainerInfoType, 'categories'>;

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
