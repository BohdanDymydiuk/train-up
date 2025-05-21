import React from 'react';

import { TrainerInfoType } from '../../types/TrainerInfoType';

import { Label } from './components/Label';
import { SecondPart } from './components/SecondPart';

import styles from './TrainerInfo.module.scss';

type Props = Omit<TrainerInfoType, 'id'>;

export const TrainerInfo: React.FC<Props> = React.memo(props => {
  const { name, categories, bio, reviews, isNew, trainingTypes } = props;

  const trainerCssProps: React.CSSProperties = isNew
    ? { marginTop: '7px' }
    : {};

  const secondPartProps = { name, trainingTypes, bio };

  return (
    <div className={styles.trainer} style={trainerCssProps}>
      {isNew && <Label />}
      <div className={styles.avatar} />
      <SecondPart {...secondPartProps} />
    </div>
  );
});
