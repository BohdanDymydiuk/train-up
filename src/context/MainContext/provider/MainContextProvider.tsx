import React, { useEffect, useMemo, useState } from 'react';
import { useMediaQuery } from 'react-responsive';
import { useLocation } from 'react-router';

import { Links } from '../../../enums/Links';
import { NavItems } from '../../../enums/NavItems';
import { MainContext } from '../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const { pathname } = useLocation();

  const [currentSection, setCurrentSection] = useState(NavItems.main);

  const onTablet = useMediaQuery({ query: '(min-width: 768px)' });
  const onDesktop = useMediaQuery({ query: '(min-width: 1200px)' });

  useEffect(() => {
    const currentDay = new Date().getDay();

    console.log(currentDay);
  }, []);

  const isTempProfile = pathname.startsWith(Links.tempProfile);

  const providerValue = useMemo(
    () => ({
      isTempProfile,
      onTablet,
      onDesktop,
      currentSection,
      setCurrentSection,
    }),
    [isTempProfile, onTablet, onDesktop, currentSection],
  );

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
