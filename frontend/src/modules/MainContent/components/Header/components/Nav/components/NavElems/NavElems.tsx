import React from 'react';
import { useLocation } from 'react-router';

import clsx from 'clsx';

import {
  HEADER_NAV_ITEMS,
  HEADER_NAV_LINKS,
} from '../../../../../../../../constants/common';
import { DropdownProps } from '../../../../../../../../reusables/DropdownHoc';

import dpStyles from '../NavDropdown/NavDropdown.module.scss';
import styles from './NavElems.module.scss';

export const NavElems: React.FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const { pathname } = useLocation();

  const isUndefined = (value: unknown) => value === undefined;

  // #region clsx
  const ulClass = clsx({
    [styles.list]: isUndefined(isDpShown),
    [dpStyles['dp-list']]: !isUndefined(isDpShown),
  });

  const aClass = clsx({
    [styles.link]: isUndefined(isDpShown),
    [dpStyles['dp-item-link']]: !isUndefined(isDpShown),
  });

  const liClass = clsx({ [dpStyles['dp-item']]: !isUndefined(isDpShown) });
  // #endregion

  return (
    <ul className={ulClass}>
      {HEADER_NAV_ITEMS.map((item, index) => {
        if (isDpShown && pathname === HEADER_NAV_LINKS[index]) {
          return;
        }

        return (
          <li key={item} className={liClass} onClick={closeDpHandler}>
            <a className={aClass}>{item}</a>
          </li>
        );
      })}
    </ul>
  );
};
