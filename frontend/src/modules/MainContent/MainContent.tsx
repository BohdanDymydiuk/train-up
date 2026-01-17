'use client';

import { FC, ReactNode, useContext, useEffect } from 'react';

import { Context } from '@/providers/context';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

import { Footer } from './components/Footer';
import { Header } from './components/Header';
import { MiniCalendar } from './components/MiniCalendar';
import { Sidebar } from './components/Sidebar';

interface Props {
  children: ReactNode;
}

export const MainContent: FC<Props> = ({ children }) => {
  const jwtToken = useAppSelector(state => state.jwtToken);

  const { onDesktop } = useContext(Context);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  return (
    <>
      <Header />
      <div
        className={clsx({
          'relative flex w-fit gap-3.5': jwtToken,
          contents: !jwtToken,
        })}
      >
        {jwtToken && onDesktop && <Sidebar />}
        <main className='overflow-x-hidden px-4 md:px-8 lg:px-12'>
          {children}
          {jwtToken && <MiniCalendar />}
        </main>
      </div>
      <Footer />
    </>
  );
};
