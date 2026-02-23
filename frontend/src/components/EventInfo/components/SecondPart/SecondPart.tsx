import { FC } from 'react';

import { ParticipantsSVG } from '@/components/svgs/sectionSvgs/events/ParticipantsSVG';
import { ThunderSVG } from '@/components/svgs/sectionSvgs/events/ThunderSVG';
import { TrainingType } from '@/components/TrainingType';
import { EventInfoType } from '@/shared/types/events';

type Props = Pick<
  EventInfoType,
  'intensity' | 'participants' | 'trainingTypes'
>;

const classes = {
  root: 'mt-4 flex justify-between',
  secondWrapper: 'flex items-center gap-4',
  thunders: 'flex items-center',
  participants: 'flex h-full items-center gap-2 px-0.75',
  number: 'font-[Inter] text-sm font-normal text-gray-500',
};

export const SecondPart: FC<Props> = ({
  intensity,
  participants,
  trainingTypes,
}) => {
  return (
    <div className={classes.root}>
      <div>
        {trainingTypes.map(type => {
          const typeProps = { type };

          return <TrainingType key={type} {...typeProps} />;
        })}
      </div>
      <div className={classes.secondWrapper}>
        <div className={classes.thunders}>
          {[...Array(3).keys()].map(number => {
            return (
              <ThunderSVG
                key={number}
                isFilled={number <= intensity ? true : false}
              />
            );
          })}
        </div>
        <div className={classes.participants}>
          <ParticipantsSVG />
          <span className={classes.number}>{participants}</span>
        </div>
      </div>
    </div>
  );
};
