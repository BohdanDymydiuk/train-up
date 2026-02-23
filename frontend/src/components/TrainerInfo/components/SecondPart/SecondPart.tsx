import { FC } from 'react';

import { TrainingType } from '@/components/TrainingType';
import { Trainer } from '@/shared/types/trainer';

type Props = Pick<Trainer, 'name' | 'trainingTypes' | 'bio'>;

const classes = {
  root: 'w-[318px] flex flex-col gap-4',
  name: 'font-[Inter] text-xl font-medium',
  types: 'flex gap-2',
  bio: 'font-[Inter] text-sm font-normal',
  read: 'font-[Inter] text-xs font-semibold',
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
