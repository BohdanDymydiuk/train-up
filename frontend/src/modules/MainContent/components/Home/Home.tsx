import React from 'react';

import { ChooseYourDirection } from './components/ChooseYourDirection';
import { SportFinder } from './components/SportFinder';
import { TrainerSignUp } from './components/TrainerSignUp';
import { WhatIsTrainUp } from './components/WhatIsTrainUp';

export const Home: React.FC = () => {
  return (
    <>
      <SportFinder />
      <WhatIsTrainUp />
      <ChooseYourDirection />
      <TrainerSignUp />
    </>
  );
};
