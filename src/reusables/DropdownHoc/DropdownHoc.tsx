import React, { useEffect, useRef, useState } from 'react';

import styles from './DropdownHoc.module.scss';

export interface ButtonProps {
  onClickHandler: () => void;
}

export interface DropdownProps {
  isDpShown?: boolean;
}

export const DropdownHoc = (
  ButtonComponent: React.FC<ButtonProps>,
  DropdownComponent: React.FC<DropdownProps>,
) => {
  const ResultedComponent: React.FC = () => {
    const wrapperRef = useRef<HTMLDivElement | null>(null);

    const [isDpActive, setIsDpActive] = useState(false);
    const [isDpShown, setIsDpShown] = useState(false);

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
        if (
          !(wrapperRef.current as Node).contains(event.target as HTMLElement)
        ) {
          closeDpHandler();
        }
      };

      if (isDpActive) {
        window.addEventListener('click', outsideClickHandler);
      }

      return () => window.removeEventListener('click', outsideClickHandler);
    }, [isDpActive]);
    // #endregion

    return (
      <div className={styles.wrapper} ref={wrapperRef}>
        <ButtonComponent onClickHandler={onClickHandler} />
        {isDpActive && <DropdownComponent isDpShown={isDpShown} />}
      </div>
    );
  };

  return ResultedComponent;
};
