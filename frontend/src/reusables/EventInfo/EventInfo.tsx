import React from 'react';

import { EventInfoType } from '../../types/EventInfoType';

import { FourthPart } from './components/FourthPart';
import { SecondPart } from './components/SecondPart';
import { ThirdPart } from './components/ThirdPart';

import styles from './EventInfo.module.scss';

type Props = Omit<EventInfoType, 'id'>;

export const EventInfo: React.FC<Props> = React.memo(props => {
  const { name, description, intensity, participants, trainingTypes, trainer } =
    props;

  const secondPartProps = { trainingTypes, intensity, participants };
  const thirdPartProps = { name, description };
  const fourthPart = { trainer };

  return (
    <div className={styles.block}>
      <div className={styles.img} />
      <SecondPart {...secondPartProps} />
      <ThirdPart {...thirdPartProps} />
      <FourthPart {...fourthPart} />
    </div>
  );
});
