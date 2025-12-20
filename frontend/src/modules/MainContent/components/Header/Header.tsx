import { CSSProperties, FC, useContext, useEffect, useState } from 'react';

import { BellSVG } from '@/components/svgs/headerSvgs/BellSVG';
import { LogoSVG } from '@/components/svgs/LogoSVG';
import { SearchSVG } from '@/components/svgs/SearchSVG';
import { DropdownHoc } from '@/hocs/DropdownHoc';
import { Context } from '@/providers/context';
import { Links } from '@/shared/enums';
import { useAppSelector } from '@/store';

import { usePathname } from 'next/navigation';

import { Burger } from './components/Burger';
import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { ProfileIMG } from './components/ProfileIMG';
import { SignInButton } from './components/SignInButton';
import { SignInDropdown } from './components/SignInDropdown';

import styles from './Header.module.scss';

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

  // #regions css props
  const firstPartCssProps: CSSProperties = { gap: '48px' };

  const secondPartCssProps: CSSProperties = { gap: '16px' };
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
