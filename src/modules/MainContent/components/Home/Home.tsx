import React from 'react';

import { Categories } from './components/Categories';
import { Events } from './components/Events';
import { SportFinder } from './components/SportFinder';
import { TrainerSignUp } from './components/TrainerSignUp';

export const Home: React.FC = () => {
  return (
    <>
      <SportFinder />
      <Categories />
      <Events />
      <TrainerSignUp />
    </>
  );
};
