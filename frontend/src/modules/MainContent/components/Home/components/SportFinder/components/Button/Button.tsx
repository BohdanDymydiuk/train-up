import { CSSProperties, FC } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';
import { ButtonProps } from '@/hocs/DropdownHoc';

import styles from '../../SportFinder.module.scss';

export const Button: FC<ButtonProps> = ({
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

  const buttonOfflineCssProps: CSSProperties = isDpShown
    ? { borderColor: styles.orange, color: styles.orange }
    : {};

  const buttonOnlineCssProps: CSSProperties = isOnline
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
