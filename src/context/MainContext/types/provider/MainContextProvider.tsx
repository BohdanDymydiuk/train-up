import React from 'react';
import { useLocation } from 'react-router';

import { NavLinks } from '../../../../enums/NavLinks';
import { MainContext } from '../../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const { pathname } = useLocation();

  const isTempProfile = pathname.startsWith(NavLinks.tempProfile);

  const providerValue = { isTempProfile };

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
