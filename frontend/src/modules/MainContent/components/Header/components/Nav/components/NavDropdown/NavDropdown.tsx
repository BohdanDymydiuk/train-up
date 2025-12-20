import { FC } from 'react';

import { APPEARING_DP_CSS_PROPS } from '@/constants/common';
import { DropdownProps } from '@/hocs/DropdownHoc';

import { NavElems } from '../NavElems';

import styles from './NavDropdown.module.scss';

export const NavDropdown: FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const dpStyle = isDpShown ? APPEARING_DP_CSS_PROPS : {};

  const navElemsProps = { isDpShown, closeDpHandler };

  return (
    <div className={styles.dropdown} style={dpStyle}>
      <NavElems {...navElemsProps} />
    </div>
  );
};
