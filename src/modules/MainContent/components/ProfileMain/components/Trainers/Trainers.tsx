import React from 'react';

import { NavItems } from '../../../../../../enums/NavItems';
import { useAppSelector } from '../../../../../../redux/store';
import { LookMore } from '../../../../../../reusables/LookMore';
import { ProfileTitle } from '../../../../../../reusables/ProfileTitle';
import { TrainerInfo } from '../../../../../../reusables/TrainerInfo';

import styles from './Trainers.module.scss';

export const Trainers: React.FC = () => {
  const trainers = useAppSelector(state => state.trainers);

  return (
    <section className={styles.trainers}>
      <ProfileTitle title={NavItems.trainers} />
      <div className={styles.wrapper}>
        {trainers.slice(0, 3).map(trainer => {
          const { id, ...trainerInfoProps } = trainer;

          return <TrainerInfo key={id} {...trainerInfoProps} />;
        })}
      </div>
      <LookMore />
    </section>
  );
};
