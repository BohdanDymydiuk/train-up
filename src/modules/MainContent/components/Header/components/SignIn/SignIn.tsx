import React, { useState } from 'react';

import { ChevronDown } from '../../../../../../reusables/ChevronDown';

import { Dropdown } from './Dropdown';

import styles from './SignIn.module.scss';

export const SignIn: React.FC = () => {
  const [isDPActive, setIsDPActive] = useState(false);
  const [isDPShown, setIsDPShown] = useState(false);

  // #region handlers

  const onClickHandler = () => {
    if (!isDPActive && !isDPShown) {
      setIsDPActive(true);
      setTimeout(() => setIsDPShown(true), 100);
    } else {
      setIsDPShown(false);
      setTimeout(() => setIsDPActive(false), 100);
    }
  };

  const onBlurHandler = () => setTimeout(() => onClickHandler(), 100);

  // #endregion
  // #region styles

  const buttonCSSProps: React.CSSProperties = {
    borderColor: `${styles.btnFocusColor}`,
  };

  const svgPathCSSProps: React.CSSProperties = {
    fill: `${styles.btnFocusColor}`,
  };

  const buttonStyles = isDPActive ? buttonCSSProps : {};
  const svgPathStyles = isDPActive ? svgPathCSSProps : {};

  // #endregion

  const dropdownProps = { isDPShown };
  const svgProps = { svgPathStyles };

  return (
    <div className={styles.wrapper}>
      <button
        className={styles.signin}
        onClick={onClickHandler}
        onBlur={onBlurHandler}
        style={buttonStyles}
      >
        <div>Увійти</div>
        <ChevronDown {...svgProps} />
      </button>
      {isDPActive && <Dropdown {...dropdownProps} />}
    </div>
  );
};
