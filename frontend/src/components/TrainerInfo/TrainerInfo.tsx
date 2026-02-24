import { FC, memo } from 'react';

import { Trainer } from '@/shared/types/trainer';

import clsx from 'clsx';

import { FourthPart } from './components/FourthPart';
import { Label } from './components/Label';
import { SecondPart } from './components/SecondPart';
import { ThirdPart } from './components/ThirdPart';

type Props = Omit<Trainer, 'id'>;

const classes = {
  trainer: clsx(
    'border-ui-gray-300 relative flex h-38 w-full items-center justify-between rounded-lg border p-4',
  ),
  avatar: clsx('bg-ui-gray-300 h-82 w-82 rounded-full'),
};

export const TrainerInfo: FC<Props> = memo(props => {
  const {
    name,
    categories = [],
    bio,
    reviews,
    isNew,
    trainingTypes = [],
  } = props;

  const secondPartProps = { name, trainingTypes, bio };
  const thirdPartProps = { categories };
  const fourthPartProps = { reviews };

  return (
    <div className={clsx(classes.trainer, { 'mt-1.75': isNew })}>
      {isNew && <Label />}
      <div className={classes.avatar} />
      <SecondPart {...secondPartProps} />
      <ThirdPart {...thirdPartProps} />
      <FourthPart {...fourthPartProps} />
    </div>
  );
});

TrainerInfo.displayName = 'TrainerInfo';
