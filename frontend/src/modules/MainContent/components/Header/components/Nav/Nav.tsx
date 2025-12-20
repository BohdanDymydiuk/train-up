import { FC, useContext, useEffect, useState } from 'react';

import { DropdownHoc } from '@/hocs/DropdownHoc';
import { Context } from '@/providers/context';

import { NavButton } from './components/NavButton';
import { NavDropdown } from './components/NavDropdown';
import { NavElems } from './components/NavElems';

export const Nav: FC = () => {
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
