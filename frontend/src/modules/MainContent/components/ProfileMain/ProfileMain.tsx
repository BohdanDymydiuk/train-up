import { FC } from 'react';

import { Events } from './components/Events';
import { Location } from './components/Location';
import { Trainers } from './components/Trainers';

export const ProfileMain: FC = () => {
  return (
    <div className='w-223'>
      <Trainers />
      <Events />
      <Location />
    </div>
  );
};
