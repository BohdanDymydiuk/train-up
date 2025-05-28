import React, { useContext, useEffect } from 'react';
import { Outlet } from 'react-router';

import { MainContext } from '../../context/MainContext';

import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { Sidebar } from './components/Sidebar';

export const MainContent: React.FC = () => {
  const { isTempProfile } = useContext(MainContext);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  const wrapperCssProps: React.CSSProperties = isTempProfile
    ? {
        display: 'flex',
        gap: '14px',
        position: 'relative',
        width: 'fit-content',
      }
    : { display: 'contents' };

  return (
    <>
      <Header />
      <div style={wrapperCssProps}>
        {isTempProfile && <Sidebar />}
        <main>
          <Outlet />
        </main>
      </div>
      <Footer />
    </>
  );
};
