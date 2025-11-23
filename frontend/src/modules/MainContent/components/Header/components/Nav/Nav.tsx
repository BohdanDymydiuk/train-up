import React, { useContext, useEffect, useState } from 'react';

import { Context } from '@/providers/context';
import { DropdownHoc } from '@/reusables/DropdownHoc';

import { NavButton } from './components/NavButton';
import { NavDropdown } from './components/NavDropdown';
import { NavElems } from './components/NavElems';

export const Nav: React.FC = () => {
  const { onTablet, onDesktop } = useContext(Context);

  const [ismounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!ismounted) {
    return null;
  }

  const NavWithDp = DropdownHoc(NavButton, NavDropdown);

  return <nav>{onTablet && !onDesktop ? <NavWithDp /> : <NavElems />}</nav>;
};
