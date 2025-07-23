import React, { useEffect } from 'react';
import { Outlet } from 'react-router';

import { useAppSelector } from '../../store/store';

import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { MiniCalendar } from './components/MiniCalendar';
import { Sidebar } from './components/Sidebar';

export const MainContent: React.FC = () => {
  const jwtToken = useAppSelector(state => state.jwtToken);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  const wrapperCssProps: React.CSSProperties = jwtToken
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
        {jwtToken && <Sidebar />}
        <main>
          <Outlet />
          {jwtToken && (
            <>
              <MiniCalendar />
            </>
          )}
        </main>
      </div>
      <Footer />
    </>
  );
};
