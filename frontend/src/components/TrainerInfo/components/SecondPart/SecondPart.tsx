import { FC } from 'react';

import { TrainingType } from '@/components/TrainingType';
import {
  TW_TYPO_INTER_400_14,
  TW_TYPO_INTER_500_20,
  TW_TYPO_INTER_600_12,
} from '@/constants/tailwind';
import { Trainer } from '@/shared/types/trainer';

import clsx from 'clsx';

type Props = Pick<Trainer, 'name' | 'trainingTypes' | 'bio'>;

const classes = {
  root: clsx('flex w-79.5 flex-col gap-4'),
  name: TW_TYPO_INTER_500_20,
  types: clsx('flex gap-2'),
  bio: TW_TYPO_INTER_400_14,
  read: TW_TYPO_INTER_600_12,
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
