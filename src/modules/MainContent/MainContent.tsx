import React, { useEffect } from 'react';

import { Outlet } from 'react-router';

import { Footer } from './components/Footer';
import { Header } from './components/Header';

export const MainContent: React.FC = () => {
  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  return (
    <>
      <Header />
      <main>
        <Outlet />
      </main>
      <Footer />
    </>
  );
};
