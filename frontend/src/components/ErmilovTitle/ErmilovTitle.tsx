import { CSSProperties, FC, JSX, useEffect, useState } from 'react';

import { TW_FONT_ERMILOV } from '@/constants/tailwind';

import clsx from 'clsx';

interface Props {
  title: JSX.Element | string;
  cssProps?: CSSProperties;
}

export const ErmilovTitle: FC<Props> = ({ title, cssProps }) => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <h2
      className={clsx(TW_FONT_ERMILOV, 'text-2xl md:text-4xl xl:text-6xl')}
      style={cssProps}
    >
      {title}
    </h2>
  );
};
