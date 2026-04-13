import { FC } from 'react';

import { TW_DROPDOWN_BASE, TW_DROPDOWN_SHOWN } from '@/constants/tailwind';
import { DropdownProps } from '@/hocs/DropdownHoc';

import clsx from 'clsx';

import { NavElems } from '../NavElems';

const classes = {
  dropdown: clsx(TW_DROPDOWN_BASE, 'top-7.5 w-(--dropdown-width)'),
  shown: TW_DROPDOWN_SHOWN,
};

export const NavDropdown: FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const navElemsProps = { isDpShown, closeDpHandler };

  return (
    <div className={clsx(classes.dropdown, { [classes.shown]: isDpShown })}>
      <NavElems {...navElemsProps} />
    </div>
  );
};
