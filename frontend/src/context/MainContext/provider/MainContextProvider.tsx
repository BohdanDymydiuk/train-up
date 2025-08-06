import React, { useEffect, useMemo, useState } from 'react';
import { useMediaQuery } from 'react-responsive';

import { NavItems } from '../../../enums/NavItems';
import { MainContext } from '../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const [currentSection, setCurrentSection] = useState(NavItems.main);

  const onTablet = useMediaQuery({ query: '(min-width: 768px)' });
  const onSmallDesktop = useMediaQuery({ query: '(min-width: 1024px)' });
  const onDesktop = useMediaQuery({ query: '(min-width: 1200px)' });

  useEffect(() => {
    const currentDay = new Date().getDay();

    console.log(currentDay);
  }, []);

  const providerValue = useMemo(
    () => ({
      onTablet,
      onSmallDesktop,
      onDesktop,
      currentSection,
      setCurrentSection,
    }),
    [onTablet, onSmallDesktop, onDesktop, currentSection],
  );

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
