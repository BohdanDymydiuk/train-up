import React, { useEffect } from 'react';

import { DropdownProps } from '@/reusables/DropdownHoc';
import { Links, NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';
import { usePathname } from 'next/navigation';

import dpStyles from '../NavDropdown/NavDropdown.module.scss';
import styles from './NavElems.module.scss';

const headerNav = {
  items: Object.values(NavItems),
  links: Object.values(Links),
};

export const NavElems: React.FC<Partial<DropdownProps>> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const pathname = usePathname();

  const jwtToken = useAppSelector(state => state.jwtToken);

  const [navItems, setNavItems] = React.useState<typeof headerNav.items>([]);
  const [navLinks, setNavLinks] = React.useState<typeof headerNav.links>([]);

  useEffect(() => {
    if (jwtToken) {
      setNavItems(headerNav.items);
      setNavLinks(headerNav.links);
    } else {
      setNavItems(headerNav.items.filter(item => item !== NavItems.calendar));
      setNavLinks(headerNav.links.filter(link => link !== Links.calendar));
      // we need NavItems.calendar for sidebar, but not for nav
    }
  }, [jwtToken]);

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

  const liClass = clsx(
    { [dpStyles['dp-item']]: !isUndefined(isDpShown) },
    styles.item,
  );
  // #endregion

  return (
    <ul className={ulClass}>
      {navItems.map((item, index) => {
        if (isDpShown && pathname === navLinks[index]) {
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
