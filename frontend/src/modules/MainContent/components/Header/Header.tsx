import { FC, useContext, useEffect, useState } from 'react';

import { BellSVG } from '@/components/svgs/headerSvgs/BellSVG';
import { LogoSVG } from '@/components/svgs/LogoSVG';
import { SearchSVG } from '@/components/svgs/SearchSVG';
import { DropdownHoc } from '@/hocs/DropdownHoc';
import { Context } from '@/providers/context';
import { Links } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';
import { usePathname } from 'next/navigation';

import { Burger } from './components/Burger';
import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { ProfileIMG } from './components/ProfileIMG';
import { SignInButton } from './components/SignInButton';
import { SignInDropdown } from './components/SignInDropdown';

const classes = {
  firstPart: clsx('flex items-center gap-12'),
  secondPart: clsx('flex items-center gap-4 [&_svg]:w-8'),
  header: clsx('flex h-40 items-center justify-between bg-white px-4 md:px-6'),
};

export const Header: FC = () => {
  const jwtToken = useAppSelector(state => state.jwtToken);

  const { onTablet, onDesktop } = useContext(Context);
  const pathname = usePathname();

  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  const SignIn = DropdownHoc(SignInButton, SignInDropdown);

  /* I don't remember why I did it, but I think I had a reason to do it like this, so I won't delete it, but I will comment it out for now */
  /* const firstPartCssProps: CSSProperties = { gap: '48px' };
  const secondPartCssProps: CSSProperties = { gap: '16px' }; */

  // #regions jsx
  // sp is "secondPart"
  const defaultSp = (
    <>
      {onTablet && <Lang />}
      {pathname !== Links.signIn && <SignIn />}
      {!onTablet && <Burger />}
    </>
  );

  const loggedSp = (
    <>
      {!onTablet ? (
        <Burger />
      ) : (
        <>
          <SearchSVG />
          <BellSVG />
          <ProfileIMG />
        </>
      )}
    </>
  );
  // #endregion

  return (
    <header className={classes.header}>
      <div className={classes.firstPart}>
        <LogoSVG />
        {jwtToken ? onTablet && !onDesktop && <Nav /> : onTablet && <Nav />}
      </div>
      <div className={classes.secondPart}>
        {jwtToken ? loggedSp : defaultSp}
      </div>
    </header>
  );
};
