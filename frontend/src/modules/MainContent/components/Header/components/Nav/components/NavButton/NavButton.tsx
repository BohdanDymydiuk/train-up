import { FC } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';
import { ButtonProps } from '@/hocs/DropdownHoc';

import styles from './NavButton.module.scss';

export const NavButton: FC<ButtonProps> = ({ onClickHandler, isDpShown }) => {
  const chevronDownSvgProps = isDpShown ? { fill: styles.orange } : {};

  return (
    <button className={styles.button} onClick={onClickHandler}>
      <div>Головна</div>
      <ChevronDownSVG {...chevronDownSvgProps} />
    </button>
  );
};
