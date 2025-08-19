import React, { useEffect, useRef, useState } from 'react';

import styles from './DropdownHoc.module.scss';

export interface DpHocProps {
  text?: string;
  items?: string[];
  select?: (value: string) => void;
}

export interface ButtonProps extends DpHocProps {
  isDpShown?: boolean;
  onClickHandler: () => void;
}

export interface DropdownProps extends DpHocProps {
  isDpShown: boolean;
  closeDpHandler: () => void;
}

export const DropdownHoc = (
  ButtonComponent: React.FC<ButtonProps>,
  DropdownComponent: React.FC<DropdownProps>,
) => {
  const ResultedComponent: React.FC<DpHocProps> = ({ text, select, items }) => {
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

    const dropdownProps: DropdownProps = { isDpShown, closeDpHandler };
    const buttonProps: ButtonProps = { onClickHandler };

    if (text) {
      dropdownProps.text = text;
      buttonProps.text = text;
    }

    if (isDpShown) buttonProps.isDpShown = isDpShown;
    if (items) dropdownProps.items = items;
    if (select) dropdownProps.select = select;

    return (
      <div className={styles.wrapper} ref={wrapperRef}>
        <ButtonComponent {...buttonProps} />
        {isDpActive && <DropdownComponent {...dropdownProps} />}
      </div>
    );
  };

  return ResultedComponent;
};
