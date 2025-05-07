import React, { useState } from 'react';

import { ChevronDown } from '../../../../../../reusables/ChevronDown';

import { Dropdown } from './Dropdown';

import styles from './SignIn.module.scss';

export const SignIn: React.FC = () => {
  const [isDPActive, setIsDPActive] = useState(false);

  const onClickHandler = () => setIsDPActive(value => !value);

  return (
    <div className={styles.wrapper}>
      <button className={styles['sign-in']} onClick={onClickHandler}>
        <div>Увійти</div>
        <ChevronDown />
      </button>
      {isDPActive && <Dropdown />}
    </div>
  );
};
