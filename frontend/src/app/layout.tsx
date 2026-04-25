import '@/globals.css';

import clsx from 'clsx';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import localFont from 'next/font/local';

import { Providers } from './providers';

// #region Fonts

const inter = Inter({ subsets: ['latin'] });

const ermilov = localFont({
  src: '../../public/fonts/Ermilov-bold.woff',
  variable: '--next-font-ermilov',
});

const wfVisualSans = localFont({
  src: '../../public/fonts/WFVisualSansVF.woff2',
  variable: '--next-font-wf-visual-sans',
});

// #endregion

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
    <html
      lang='en'
      className={clsx(inter.className, ermilov.variable, wfVisualSans.variable)}
    >
      <body>
        <div id='root'>
          <Providers>{children}</Providers>
        </div>
      </body>
    </html>
  );
}
