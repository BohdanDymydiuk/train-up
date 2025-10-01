'use client';

import dynamic from 'next/dynamic';

const App = dynamic(() => import('../../App' as string), { ssr: false });

export function ClientOnly() {
  return <App />;
}
