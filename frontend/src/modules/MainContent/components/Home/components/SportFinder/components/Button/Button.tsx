import React from 'react';

import { ButtonProps } from '../../../../../../../../reusables/DropdownHoc';
import { ChevronDownSVG } from '../../../../../../../../reusables/svgs/ChevronDownSVG';

import styles from '../../SportFinder.module.scss';

export const Button: React.FC<ButtonProps> = ({ onClickHandler, text }) => {
  const chevronDownSvgCssProps = {
    svgStyle: { minWidth: '16px' },
    pathStyle: { fill: `${styles.gray}` },
  };

  return (
    <button className={styles.select} onClick={onClickHandler}>
      <div className={styles.text}>{text}</div>
      <ChevronDownSVG {...chevronDownSvgCssProps} />
    </button>
  );
};
