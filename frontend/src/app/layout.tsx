import type { Metadata } from 'next';

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
        <div id='root'>{children}</div>
        <script type='module' src='/src/index.tsx'></script>
      </body>
    </html>
  );
}
