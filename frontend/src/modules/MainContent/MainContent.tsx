'use client';

import React, { useEffect } from 'react';

import { Context } from '@/providers/context';
import { useAppSelector } from '@/store';

import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { MiniCalendar } from './components/MiniCalendar';
import { Sidebar } from './components/Sidebar';

import styles from './MainContent.module.scss';

interface Props {
  children: React.ReactNode;
}

export const MainContent: React.FC<Props> = ({ children }) => {
  const jwtToken = useAppSelector(state => state.jwtToken);

  const { onDesktop } = React.useContext(Context);

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
        {jwtToken && onDesktop && <Sidebar />}
        <main className={styles.main}>
          {children}
          {jwtToken && <MiniCalendar />}
        </main>
      </div>
      <Footer />
    </>
  );
};
