import React from 'react';

import { ButtonProps } from '@/reusables/DropdownHoc';
import { ChevronDownSVG } from '@/reusables/svgs/ChevronDownSVG';

import styles from './NavButton.module.scss';

export const NavButton: React.FC<ButtonProps> = ({
  onClickHandler,
  isDpShown,
}) => {
  const chevronDownSvgProps = isDpShown ? { fill: styles.orange } : {};

  return (
    <button className={styles.button} onClick={onClickHandler}>
      <div>Головна</div>
      <ChevronDownSVG {...chevronDownSvgProps} />
    </button>
  );
};
