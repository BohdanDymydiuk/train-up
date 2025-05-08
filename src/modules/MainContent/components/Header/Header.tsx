import React from 'react';

import { Logo } from '../../../../reusables/Logo';

import { Lang } from './components/Lang';
import { Nav } from './components/Nav';
import { SignIn } from './components/SignIn';

import styles from './Header.module.scss';

export const Header: React.FC = () => {
  return (
    <header className={styles.header}>
      <div className={styles['first-part']}>
        <Logo />
        <Nav />
      </div>
      <div className={styles['second-part']}>
        <Lang />
        <SignIn />
      </div>
    </header>
  );
};
