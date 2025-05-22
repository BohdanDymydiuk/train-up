import React from 'react';

import { Events } from './components/Events';
import { Trainers } from './components/Trainers';

export const ProfileMain: React.FC = () => {
  return (
    <>
      <Trainers />
      <Events />
    </>
  );
};
