import { FC } from 'react';

import { TrainingType } from '@/components/TrainingType';
import { Trainer } from '@/shared/types/trainer';

import clsx from 'clsx';

type Props = Pick<Trainer, 'name' | 'trainingTypes' | 'bio'>;

const classes = {
  root: clsx('flex w-79.5 flex-col gap-4'),
  name: clsx('font-[Inter] text-xl font-medium'),
  types: clsx('flex gap-2'),
  bio: clsx('font-[Inter] text-sm font-normal'),
  read: clsx('font-[Inter] text-xs font-semibold'),
};

export const SecondPart: FC<Props> = props => {
  const { name, bio, trainingTypes } = props;

  return (
    <div className={classes.root}>
      <h3 className={classes.name}>{name}</h3>
      <div className={classes.types}>
        {trainingTypes.map(type => {
          const typeProps = { type };

          return <TrainingType key={type} {...typeProps} />;
        })}
      </div>
      <div className={classes.bio}>{bio}</div>
      <div className={classes.read}>Читати більше</div>
    </div>
  );
};
