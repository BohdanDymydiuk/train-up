'use client';

import { ReactNode } from 'react';
import { Provider } from 'react-redux';

import { ContextProvider } from '@/providers/context';
import { store } from '@/store';

import { AppClient } from './AppClient';

// створимо далі

export function Providers({ children }: { children: ReactNode }) {
  return (
    <Provider store={store}>
      <ContextProvider>
        <AppClient />
        {children}
      </ContextProvider>
    </Provider>
  );
}
