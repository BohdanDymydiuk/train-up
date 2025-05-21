import React from 'react';

import { TrainerInfoType } from '../../types/TrainerInfoType';

import { SecondPart } from './components/SecondPart';

import styles from './TrainerInfo.module.scss';

type Props = Omit<TrainerInfoType, 'id'>;

export const TrainerInfo: React.FC<Props> = React.memo(props => {
  const { name, categories, bio, reviews, isNew, trainingTypes } = props;

  const firstPartProps = { name, trainingTypes, bio };

  return (
    <div className={styles.trainer}>
      <div className={styles.avatar} />
      <SecondPart {...firstPartProps} />
    </div>
  );
});
