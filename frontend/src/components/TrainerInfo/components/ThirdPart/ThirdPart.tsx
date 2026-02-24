import { FC } from 'react';

import { Trainer } from '@/shared/types/trainer';

import clsx from 'clsx';

import { Category } from './components/Category';

type Props = Pick<Trainer, 'categories'>;

const classes = {
  root: clsx('flex w-51 flex-wrap gap-4'),
};

export const ThirdPart: FC<Props> = ({ categories }) => {
  return (
    <div className={classes.root}>
      {categories.map(category => {
        const categoryProps = { category };

        return <Category key={category} {...categoryProps} />;
      })}
    </div>
  );
};
