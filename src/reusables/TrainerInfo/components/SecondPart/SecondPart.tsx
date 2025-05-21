import React from 'react';

import { TrainerInfoType } from '../../../../types/TrainerInfoType';

import styles from './SecondPart.module.scss';

type Props = Pick<TrainerInfoType, 'name' | 'trainingTypes' | 'bio'>;

export const SecondPart: React.FC<Props> = props => {
  const { name, bio, trainingTypes } = props;

  const offlineCssProps: React.CSSProperties = {
    border: `1px solid ${styles.brownColor}`,
  };

  const onlineCssProps: React.CSSProperties = {
    border: `1px solid ${styles.darkColor}`,
    backgroundColor: styles.darkColor,
    color: '#F4DCDC',
  };

  return (
    <div className={styles['second-part']}>
      <h3 className={styles.name}>{name}</h3>
      <div className={styles.types}>
        {trainingTypes.map(type => {
          const typeStyle =
            type === 'Онлайн' ? onlineCssProps : offlineCssProps;

          return (
            <div key={type} className={styles.type} style={typeStyle}>
              {type}
            </div>
          );
        })}
      </div>
      <div className={styles.bio}>{bio}</div>
      <div className={styles.read}>Читати більше</div>
    </div>
  );
};
