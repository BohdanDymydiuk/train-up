import React, { useContext } from 'react';
import { useLocation } from 'react-router';

import { Links } from '@/enums/Links';
import { Context } from '@/providers/context';
import { DropdownHoc } from '@/reusables/DropdownHoc';
import { BellSVG } from '@/reusables/svgs/headerSvgs/BellSVG';
import { SearchSVG } from '@/reusables/svgs/headerSvgs/SearchSVG';
import { LogoSVG } from '@/reusables/svgs/LogoSVG';
import { useAppSelector } from '@/store';

import { Burger } from './components/Burger';
import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { ProfileIMG } from './components/ProfileIMG';
import { SignInButton } from './components/SignInButton';
import { SignInDropdown } from './components/SignInDropdown';

import styles from './Header.module.scss';

export const Header: React.FC = () => {
  const jwtToken = useAppSelector(state => state.jwtToken);

  const { onTablet, onDesktop } = useContext(Context);
  const { pathname } = useLocation();

  const SignIn = DropdownHoc(SignInButton, SignInDropdown);

  // #regions css props
  const firstPartCssProps: React.CSSProperties = { gap: '48px' };

  const secondPartCssProps: React.CSSProperties = { gap: '16px' };
  // #endregion

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
    <header className={styles.header}>
      <div className={styles['first-part']} style={firstPartCssProps}>
        <LogoSVG />
        {jwtToken ? onTablet && !onDesktop && <Nav /> : onTablet && <Nav />}
      </div>
      <div className={styles['second-part']} style={secondPartCssProps}>
        {jwtToken ? loggedSp : defaultSp}
      </div>
    </header>
  );
};
