import '@/globals.css';

import type { Metadata } from 'next';

import { Providers } from './providers';

export const metadata: Metadata = {
  title: 'TrainUp',
  description: 'Your personal training companion',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang='en'>
      <head>
        <link rel='preconnect' href='https://fonts.googleapis.com' />
        <link
          rel='preconnect'
          href='https://fonts.gstatic.com'
          crossOrigin=''
        />
      </head>
      <body>
        <div id='root'>
          <Providers>{children}</Providers>
        </div>
      </body>
    </html>
  );
}
