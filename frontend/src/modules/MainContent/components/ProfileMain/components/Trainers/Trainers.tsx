import { FC } from 'react';

import { LookMore } from '@/components/LookMore';
import { ProfileTitle } from '@/components/ProfileTitle';
import { TrainerInfo } from '@/components/TrainerInfo';
import { NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import styles from './Trainers.module.scss';

export const Trainers: FC = () => {
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
