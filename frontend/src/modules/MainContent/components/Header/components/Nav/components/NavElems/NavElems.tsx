import { FC, useEffect, useState } from 'react';

import {
  TW_DROPDOWN_ITEM,
  TW_DROPDOWN_ITEM_LINK,
  TW_DROPDOWN_LIST,
  TW_TYPO_INTER_400_16,
} from '@/constants/tailwind';
import { DropdownProps } from '@/hocs/DropdownHoc';
import { Links, NavItems } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';
import { usePathname } from 'next/navigation';

const headerNav = {
  items: Object.values(NavItems),
  links: Object.values(Links),
};

const classes = {
  list: clsx('flex list-none gap-10.5'),
  item: clsx('cursor-pointer'),
  link: clsx('text-dark', TW_TYPO_INTER_400_16),
  dpList: TW_DROPDOWN_LIST,
  dpItem: TW_DROPDOWN_ITEM,
  dpItemLink: TW_DROPDOWN_ITEM_LINK,
};

export const NavElems: FC<Partial<DropdownProps>> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const pathname = usePathname();

  const jwtToken = useAppSelector(state => state.jwtToken);

  const [navItems, setNavItems] = useState<typeof headerNav.items>([]);
  const [navLinks, setNavLinks] = useState<typeof headerNav.links>([]);

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
    [classes.list]: isUndefined(isDpShown),
    [classes.dpList]: !isUndefined(isDpShown),
  });

  const aClass = clsx({
    [classes.link]: isUndefined(isDpShown),
    [classes.dpItemLink]: !isUndefined(isDpShown),
  });

  const liClass = clsx(
    { [classes.dpItem]: !isUndefined(isDpShown) },
    classes.item,
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
