'use client';

import { Provider } from 'react-redux';

import { store } from '@/store';

import dynamic from 'next/dynamic';

const App = dynamic(() => import('@/App' as string), { ssr: false });

export function ClientOnly() {
  return (
    <Provider store={store}>
      <App />
    </Provider>
  );
}
