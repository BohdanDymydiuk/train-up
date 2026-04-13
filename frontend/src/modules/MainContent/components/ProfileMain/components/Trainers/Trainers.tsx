import { FC } from 'react';

import { LookMore } from '@/components/LookMore';
import { ProfileTitle } from '@/components/ProfileTitle';
import { TrainerInfo } from '@/components/TrainerInfo';
import { NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

const classes = {
  trainers: clsx('pt-7'),
  wrapper: clsx('mt-6 flex flex-col gap-8.25'),
};

export const Trainers: FC = () => {
  const trainers = useAppSelector(state => state.trainers);

  return (
    <section className={classes.trainers}>
      <ProfileTitle title={NavItems.trainers} />
      <div className={classes.wrapper}>
        {trainers.slice(0, 3).map(trainer => {
          const { id, ...trainerInfoProps } = trainer;

          return <TrainerInfo key={id} {...trainerInfoProps} />;
        })}
      </div>
      <LookMore />
    </section>
  );
};
