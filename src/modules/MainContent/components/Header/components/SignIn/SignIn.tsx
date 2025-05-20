import React, { useEffect, useRef, useState } from 'react';

import { ChevronDown } from '../../../../../../reusables/ChevronDown';

import { Dropdown } from './Dropdown';

import styles from './SignIn.module.scss';

export const SignIn: React.FC = () => {
  const [isDpActive, setIsDpActive] = useState(false);
  const [isDpShown, setIsDpShown] = useState(false);

  const wrapperRef = useRef<HTMLDivElement | null>(null);

  // #region handlers

  const closeDpHandler = () => {
    setIsDpShown(false);
    setTimeout(() => setIsDpActive(false), 100);
  };

  const onClickHandler = () => {
    if (!isDpActive && !isDpShown) {
      setIsDpActive(true);
      setTimeout(() => setIsDpShown(true), 100);
    } else {
      closeDpHandler();
    }
  };

  // #endregion
  // #region useEffects

  useEffect(() => {
    const outsideClickHandler = (event: MouseEvent) => {
      if (!(wrapperRef.current as Node).contains(event.target as HTMLElement)) {
        closeDpHandler();
      }
    };

    if (isDpActive) {
      window.addEventListener('click', outsideClickHandler);
    }

    return () => window.removeEventListener('click', outsideClickHandler);
  }, [isDpActive]);

  // #endregion
  // #region styles

  const buttonCssProps: React.CSSProperties = {
    borderColor: `${styles.btnFocusColor}`,
  };

  const svgPathCssProps: React.CSSProperties = {
    fill: `${styles.btnFocusColor}`,
  };

  const buttonStyles = isDpActive ? buttonCssProps : {};
  const svgPathStyles = isDpActive ? svgPathCssProps : {};

  // #endregion

  const dropdownProps = { isDpShown };
  const svgProps = { svgPathStyles };

  return (
    <div className={styles.wrapper} ref={wrapperRef}>
      <button
        className={styles.signin}
        onClick={onClickHandler}
        style={buttonStyles}
      >
        <div>Увійти</div>
        <ChevronDown {...svgProps} />
      </button>
      {isDpActive && <Dropdown {...dropdownProps} />}
    </div>
  );
};
