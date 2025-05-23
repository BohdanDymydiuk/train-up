import React from 'react';

import { Events } from './components/Events';
import { Trainers } from './components/Trainers';

import styles from './ProfileMain.module.scss';

export const ProfileMain: React.FC = () => {
  return (
    <div className={styles['profile-main']}>
      <Trainers />
      <Events />
    </div>
  );
};
