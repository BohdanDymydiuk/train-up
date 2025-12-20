import { FC } from 'react';

import { Events } from './components/Events';
import { Location } from './components/Location';
import { Trainers } from './components/Trainers';

import styles from './ProfileMain.module.scss';

export const ProfileMain: FC = () => {
  return (
    <div className={styles['profile-main']}>
      <Trainers />
      <Events />
      <Location />
    </div>
  );
};
