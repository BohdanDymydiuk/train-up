'use client';

import { FC } from 'react';

import { AUTH_STRINGS } from '@/constants/strings';
import {
  TW_DROPDOWN_BASE,
  TW_DROPDOWN_ITEM,
  TW_DROPDOWN_ITEM_LINK,
  TW_DROPDOWN_LIST,
  TW_DROPDOWN_SHOWN,
} from '@/constants/tailwind';
import { DropdownProps } from '@/hocs/DropdownHoc';
import { Links } from '@/shared/enums';

import clsx from 'clsx';
import { useRouter } from 'next/navigation';

const SignInOptions = [
  AUTH_STRINGS.signInAsClient,
  AUTH_STRINGS.signInAsTrainer,
  AUTH_STRINGS.signInAsGymAdmin,
] as const;

const classes = {
  dropdown: clsx(
    TW_DROPDOWN_BASE,
    'top-[calc(var(--signin-height)+16px)] left-[calc((var(--dropdown-width)-var(--signin-width))*-1)] w-(--dropdown-width)',
  ),
  shown: TW_DROPDOWN_SHOWN,
  list: TW_DROPDOWN_LIST,
  item: TW_DROPDOWN_ITEM,
  link: TW_DROPDOWN_ITEM_LINK,
};

export const SignInDropdown: FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
}) => {
  const router = useRouter();

  const signInHandler = () => {
    router.push(Links.signIn);

    if (closeDpHandler) {
      closeDpHandler();
    }
  };

  return (
    <div className={clsx(classes.dropdown, { [classes.shown]: isDpShown })}>
      <ul className={classes.list}>
        {SignInOptions.map(item => {
          return (
            <li className={classes.item} key={item} onClick={signInHandler}>
              <a className={classes.link}>{item}</a>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
