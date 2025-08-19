import React from 'react';

import { ButtonProps } from '../../../../../../../../reusables/DropdownHoc';
import { ChevronDownSVG } from '../../../../../../../../reusables/svgs/ChevronDownSVG';

import styles from '../../SportFinder.module.scss';

export const Button: React.FC<ButtonProps> = ({
  onClickHandler,
  text,
  isDpShown,
}) => {
  // #region props
  const chevronDownSvgProps = {
    svgStyle: { minWidth: '16px' },
    fill: isDpShown ? styles.orange : styles.gray,
  };

  const buttonCssProps: React.CSSProperties = isDpShown
    ? { borderColor: styles.orange }
    : {};

  const textCssProps: React.CSSProperties = isDpShown
    ? { color: styles.orange }
    : {};
  // #endregion

  return (
    <button
      className={styles.select}
      style={buttonCssProps}
      onClick={onClickHandler}
    >
      <div className={styles.text} style={textCssProps}>
        {text}
      </div>
      <ChevronDownSVG {...chevronDownSvgProps} />
    </button>
  );
};
