import { FC } from 'react';

import { TrainingType } from '@/components/TrainingType';
import { Trainer } from '@/shared/types/trainer';

import styles from './SecondPart.module.scss';

type Props = Pick<Trainer, 'name' | 'trainingTypes' | 'bio'>;

export const SecondPart: FC<Props> = props => {
  const { name, bio, trainingTypes } = props;

  return (
    <div className={styles['second-part']}>
      <h3 className={styles.name}>{name}</h3>
      <div className={styles.types}>
        {trainingTypes.map(type => {
          const typeProps = { type };

          return <TrainingType key={type} {...typeProps} />;
        })}
      </div>
      <div className={styles.bio}>{bio}</div>
      <div className={styles.read}>Читати більше</div>
    </div>
  );
};
