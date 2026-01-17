import { FC } from 'react';

import { ParticipantsSVG } from '@/components/svgs/sectionSvgs/events/ParticipantsSVG';
import { ThunderSVG } from '@/components/svgs/sectionSvgs/events/ThunderSVG';
import { TrainingType } from '@/components/TrainingType';
import { EventInfoType } from '@/shared/types/events';

import styles from './SecondPart.module.css';

type Props = Pick<
  EventInfoType,
  'intensity' | 'participants' | 'trainingTypes'
>;

export const SecondPart: FC<Props> = ({
  intensity,
  participants,
  trainingTypes,
}) => {
  return (
    <div className={styles['second-part']}>
      <div className={styles['types-wrapper']}>
        {trainingTypes.map(type => {
          const typeProps = { type };

          return <TrainingType key={type} {...typeProps} />;
        })}
      </div>
      <div className={styles['second-wrapper']}>
        <div className={styles.thunders}>
          {[...Array(3).keys()].map(number => {
            return (
              <ThunderSVG
                key={number}
                isFilled={number <= intensity ? true : false}
              />
            );
          })}
        </div>
        <div className={styles.participants}>
          <ParticipantsSVG />
          <span className={styles.number}>{participants}</span>
        </div>
      </div>
    </div>
  );
};
