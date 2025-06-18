import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';

import { NavElems } from './components/NavElems';

export const Nav: React.FC = () => {
  const { onTablet, onDesktop } = useContext(MainContext);

  return <nav>{onTablet && !onDesktop ? <></> : <NavElems />}</nav>;
};
