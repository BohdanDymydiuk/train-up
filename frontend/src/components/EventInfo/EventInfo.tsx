import { FC, memo } from 'react';

import { EventInfoType } from '@/shared/types/events';

import clsx from 'clsx';

import { FourthPart } from './components/FourthPart';
import { SecondPart } from './components/SecondPart';
import { ThirdPart } from './components/ThirdPart';

type Props = Omit<EventInfoType, 'id'>;

const classes = {
  root: clsx('border-ui-gray-500 rounded-lg border p-4'),
  image: clsx('bg-ui-gray-500 h-47 w-100 rounded-2xl'),
};

export const EventInfo: FC<Props> = memo(props => {
  const {
    name,
    description,
    intensity,
    participants,
    trainingTypes = [],
    trainer,
  } = props;

  const secondPartProps = { trainingTypes, intensity, participants };
  const thirdPartProps = { name, description };
  const fourthPart = { trainer };

  return (
    <div className={classes.root}>
      <div className={classes.image} />
      <SecondPart {...secondPartProps} />
      <ThirdPart {...thirdPartProps} />
      <FourthPart {...fourthPart} />
    </div>
  );
});

EventInfo.displayName = 'EventInfo';
