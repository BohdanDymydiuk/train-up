import { CSSProperties, FC } from 'react';

import { ChevronDownSVG } from '@/components/svgs/ChevronDownSVG';
import { ButtonProps } from '@/hocs/DropdownHoc';

import styles from '../../SportFinder.module.css';

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
      (isOnline && styles['color-gray-200']) ||
      (isDpShown && styles['color-accent']) ||
      styles['color-gray-900'],
  };

  const buttonOfflineCssProps: CSSProperties = isDpShown
    ? { borderColor: styles['color-accent'], color: styles['color-accent'] }
    : {};

  const buttonOnlineCssProps: CSSProperties = isOnline
    ? { color: styles['color-gray-200'], cursor: 'not-allowed' }
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
