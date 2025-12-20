import { FC } from 'react';

import { ChooseYourDirection } from './components/ChooseYourDirection';
import { Events } from './components/Events';
import { SportFinder } from './components/SportFinder';
import { TrainerSignUp } from './components/TrainerSignUp';
import { WhatIsTrainUp } from './components/WhatIsTrainUp';

export const Home: FC = () => {
  return (
    <>
      <SportFinder />
      <WhatIsTrainUp />
      <ChooseYourDirection />
      <Events />
      <TrainerSignUp />
    </>
  );
};
