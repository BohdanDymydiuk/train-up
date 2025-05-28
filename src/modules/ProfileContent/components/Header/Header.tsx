import React, { useContext } from 'react';
import { Link } from 'react-router';

import { MainContext } from '../../../../context/MainContext';
import { NavLinks } from '../../../../enums/NavLinks';
import { Logo } from '../../../../reusables/Logo';
import { BellSVG } from '../../../../reusables/svgs/BellSVG';
import { SearchSVG } from '../../../../reusables/svgs/SearchSVG';

import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { ProfileIMG } from './components/ProfileIMG';
import { SignIn } from './components/SignIn';
import { Title } from './components/Title';

import styles from './Header.module.scss';

export const Header: React.FC = () => {
  const { isTempProfile } = useContext(MainContext);

  // #regions css props

  const firstPartCssProps: React.CSSProperties = isTempProfile
    ? { gap: '15px' }
    : { gap: '20px' };

  const secondPartCssProps: React.CSSProperties = isTempProfile
    ? { gap: '32px' }
    : { gap: '8px' };

  // #endregion
  // #regions jsx

  const defaultSp = (
    <>
      <Lang />
      <SignIn />
      <Link to={NavLinks.tempProfile}>TempProfile</Link>
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
        <Logo />
        {isTempProfile ? <Title /> : <Nav />}
      </div>
      <div className={styles['second-part']} style={secondPartCssProps}>
        {isTempProfile ? tempSp : defaultSp}
      </div>
    </header>
  );
};
