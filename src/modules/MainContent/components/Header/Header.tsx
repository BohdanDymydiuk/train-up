import React, { useContext } from 'react';

import { MainContext } from '../../../../context/MainContext';
import { DropdownHoc } from '../../../../reusables/DropdownHoc';
import { BellSVG } from '../../../../reusables/svgs/headerSvgs/BellSVG';
import { SearchSVG } from '../../../../reusables/svgs/headerSvgs/SearchSVG';
import { LogoSVG } from '../../../../reusables/svgs/LogoSVG';

import { Burger } from './components/Burger';
import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { ProfileIMG } from './components/ProfileIMG';
import { SignInButton } from './components/SignInButton';
import { SignInDropdown } from './components/SignInDropdown';

import styles from './Header.module.scss';

export const Header: React.FC = () => {
  const { isTempProfile, onTablet } = useContext(MainContext);

  const SignIn = DropdownHoc(SignInButton, SignInDropdown);

  // #regions css props
  const firstPartCssProps: React.CSSProperties = isTempProfile
    ? { gap: '15px' }
    : { gap: '20px' };

  const secondPartCssProps: React.CSSProperties = isTempProfile
    ? { gap: '32px' }
    : { gap: '16px' };
  // #endregion

  // #regions jsx
  // sp is "secondPart"
  const defaultSp = (
    <>
      {onTablet && <Lang />}
      <SignIn />
      {!onTablet && <Burger />}
      {/* <Link to={Links.tempProfile}>TempProfile</Link> */}
    </>
  );

  const tempSp = (
    <>
      <SearchSVG />
      <BellSVG />
      <ProfileIMG />
    </>
  );
  // #endregion

  return (
    <header className={styles.header}>
      <div className={styles['first-part']} style={firstPartCssProps}>
        <LogoSVG />
        {onTablet && <Nav />}
      </div>
      <div className={styles['second-part']} style={secondPartCssProps}>
        {isTempProfile ? tempSp : defaultSp}
      </div>
    </header>
  );
};
