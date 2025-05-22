import React from 'react';

import { EventInfoType } from '../../types/EventInfoType';

import { SecondPart } from './components/SecondPart';

import styles from './EventInfo.module.scss';

type Props = Omit<EventInfoType, 'id'>;

export const EventInfo: React.FC<Props> = React.memo(props => {
  const { name, description, intensity, participants, trainingTypes, trainer } =
    props;

  const secondPartProps = { trainingTypes, intensity, participants };

  return (
    <div className={styles.block}>
      <div className={styles.img} />
      <SecondPart {...secondPartProps} />
    </div>
  );
});
