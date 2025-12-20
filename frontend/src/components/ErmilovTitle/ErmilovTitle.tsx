import { CSSProperties, FC, JSX, useEffect, useState } from 'react';

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
      className='font-[Ermilov] text-2xl md:text-4xl xl:text-6xl'
      style={cssProps}
    >
      {title}
    </h2>
  );
};
