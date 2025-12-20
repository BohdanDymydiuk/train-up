import { CSSProperties, FC, memo } from 'react';

import { Trainer } from '@/shared/types/trainer';

import { FourthPart } from './components/FourthPart';
import { Label } from './components/Label';
import { SecondPart } from './components/SecondPart';
import { ThirdPart } from './components/ThirdPart';

import styles from './TrainerInfo.module.scss';

type Props = Omit<Trainer, 'id'>;

export const TrainerInfo: FC<Props> = memo(props => {
  const {
    name,
    categories = [],
    bio,
    reviews,
    isNew,
    trainingTypes = [],
  } = props;

  console.log(props);

  const trainerCssProps: CSSProperties = isNew ? { marginTop: '7px' } : {};

  const secondPartProps = { name, trainingTypes, bio };
  const thirdPartProps = { categories };
  const fourthPartProps = { reviews };

  return (
    <div className={styles.trainer} style={trainerCssProps}>
      {isNew && <Label />}
      <div className={styles.avatar} />
      <SecondPart {...secondPartProps} />
      <ThirdPart {...thirdPartProps} />
      <FourthPart {...fourthPartProps} />
    </div>
  );
});

TrainerInfo.displayName = 'TrainerInfo';
