import React from 'react';

import { ButtonProps } from '../../../../../../../../reusables/DropdownHoc';
import { ChevronDownSVG } from '../../../../../../../../reusables/svgs/ChevronDownSVG';

import styles from './NavButton.module.scss';

export const NavButton: React.FC<ButtonProps> = ({ onClickHandler }) => {
  return (
    <button className={styles.button} onClick={onClickHandler}>
      <div>Головна</div>
      <ChevronDownSVG />
    </button>
  );
};
