import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { NavItems } from '../../../../../../enums/NavItems';
import { LookMore } from '../../../../../../reusables/LookMore';
import { ProfileTitle } from '../../../../../../reusables/ProfileTitle';
import { TrainerInfo } from '../../../../../../reusables/TrainerInfo';

import styles from './Trainers.module.scss';

export const Trainers: React.FC = () => {
  const { trainers } = useContext(MainContext);

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
