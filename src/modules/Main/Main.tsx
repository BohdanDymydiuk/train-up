import React, { useEffect } from 'react';

import { Categories } from './components/Categories';
import { Events } from './components/Events';
import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { SportFinder } from './components/SportFinder';
import { TrainerSignUp } from './components/TrainerSignUp';

export const Main: React.FC = () => {
  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  return (
    <>
      <Header />
      <main>
        <SportFinder />
        <Categories />
        <Events />
        <TrainerSignUp />
      </main>
      <Footer />
    </>
  );
};
