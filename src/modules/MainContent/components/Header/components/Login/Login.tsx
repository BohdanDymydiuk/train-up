import React, { useState } from 'react';

import { ChevronDown } from '../../../../../../reusables/ChevronDown';

import { Dropdown } from './Dropdown';

import styles from './Login.module.scss';

export const Login: React.FC = () => {
  const [isDPActive, setIsDPActive] = useState(false);

  const onClickHandler = () => setIsDPActive(value => !value);

  return (
    <div className={styles.wrapper}>
      <button className={styles.login} onClick={onClickHandler}>
        <div>Увійти</div>
        <ChevronDown />
      </button>
      {isDPActive && <Dropdown />}
    </div>
  );
};
