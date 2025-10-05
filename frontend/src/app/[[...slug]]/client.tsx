'use client';

import { Provider } from 'react-redux';

import dynamic from 'next/dynamic';

import { store } from '../../store/store';

const App = dynamic(() => import('../../App' as string), { ssr: false });

export function ClientOnly() {
  return (
    <Provider store={store}>
      <App />
    </Provider>
  );
}
