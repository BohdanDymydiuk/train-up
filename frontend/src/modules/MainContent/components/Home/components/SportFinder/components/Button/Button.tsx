import React from 'react';

import { ButtonProps } from '@/reusables/DropdownHoc';
import { ChevronDownSVG } from '@/reusables/svgs/ChevronDownSVG';

import styles from '../../SportFinder.module.scss';

export const Button: React.FC<ButtonProps> = ({
  onClickHandler,
  text,
  isDpShown,
  isOnline,
}) => {
  // #region props
  const chevronDownSvgProps = {
    svgStyle: { minWidth: '16px' },
    fill:
      (isOnline && styles.gray2) || (isDpShown && styles.orange) || styles.gray,
  };

  const buttonOfflineCssProps: React.CSSProperties = isDpShown
    ? { borderColor: styles.orange, color: styles.orange }
    : {};

  const buttonOnlineCssProps: React.CSSProperties = isOnline
    ? { color: styles.gray2, cursor: 'not-allowed' }
    : {};

  const buttonCssProps = { ...buttonOfflineCssProps, ...buttonOnlineCssProps };
  // #endregion

  return (
    <button
      className={styles.select}
      style={buttonCssProps}
      onClick={isOnline ? () => {} : onClickHandler}
    >
      <div>{text}</div>
      <ChevronDownSVG {...chevronDownSvgProps} />
    </button>
  );
};
