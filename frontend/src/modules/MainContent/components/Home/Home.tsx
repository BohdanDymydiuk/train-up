import React from 'react';

import { Events } from './components/Events';
import { SportFinder } from './components/SportFinder';
import { TrainerSignUp } from './components/TrainerSignUp';
import { WhatIsTrainUp } from './components/WhatIsTrainUp';

export const Home: React.FC = () => {
  return (
    <>
      <SportFinder />
      <WhatIsTrainUp />
      <Events />
      <TrainerSignUp />
    </>
  );
};
