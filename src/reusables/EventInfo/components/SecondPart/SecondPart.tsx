import React from 'react';

import { EventInfoType } from '../../../../types/EventInfoType';
import { ParticipantsSVG } from '../../../svgs/ParticipantsSVG';
import { ThunderSVG } from '../../../svgs/ThunderSVG';
import { TrainingType } from '../../../TrainingType';

import styles from './SecondPart.module.scss';

type Props = Pick<
  EventInfoType,
  'intensity' | 'participants' | 'trainingTypes'
>;

export const SecondPart: React.FC<Props> = ({
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
