import React, { useContext, useMemo, useState } from 'react';
import Highlighter from 'react-highlight-words';

import { APPEARING_DP_CSS_PROPS } from '../../../../../../../../constants/common';
import { MainContext } from '../../../../../../../../context/MainContext';
import { FinderTexts } from '../../../../../../../../enums/FinderTexts';
import { DropdownProps } from '../../../../../../../../reusables/DropdownHoc';
import { SearchSVG } from '../../../../../../../../reusables/svgs/SearchSVG';

import styles from './Dropdown.module.scss';

export const Dropdown: React.FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
  items,
  select,
  text,
}) => {
  const { onTablet, onDesktop } = useContext(MainContext);

  const [query, setSquery] = useState('');
  const [placeholder, setPlaceholder] = useState('Пошук');

  const onClickInputHandler = () => setPlaceholder('');

  const fileredItems = useMemo(() => {
    return items?.filter(item =>
      item.toLowerCase().includes(query.toLowerCase()),
    );
  }, [query]);

  const dpCssProps: React.CSSProperties = useMemo(() => {
    const result: React.CSSProperties = {
      width: `calc(100% + ${styles.finderPadding}${!onTablet && '* 2'})`,
      left: `calc(${styles.finderPadding} * -1)`,
    };

    if (onTablet) {
      result.width = `calc(100% + ${styles.finderPadding})`;

      if (text === FinderTexts.city) {
        result.left = '0';

        if (onDesktop) result.width = '125%';
      }
    }

    return result;
  }, [onTablet, onDesktop]);

  const dpStyle = isDpShown
    ? { ...APPEARING_DP_CSS_PROPS, ...dpCssProps }
    : dpCssProps;

  const onClickHandler = (name: string) => {
    if (select) select(name);
    if (closeDpHandler) closeDpHandler();
  };

  return (
    <div className={styles.dropdown} style={dpStyle}>
      <div className={styles['search-wrapper']}>
        <SearchSVG />

        <input
          className={styles.search}
          type='text'
          value={query}
          onChange={event => setSquery(event.target.value)}
          onClick={onClickInputHandler}
          placeholder={placeholder}
        />
      </div>
      {fileredItems && fileredItems.length > 0 && (
        <ul className={styles['dp-list']}>
          {fileredItems.map(item => {
            return (
              <li
                key={item}
                className={styles['dp-item']}
                onClick={() => onClickHandler(item)}
              >
                <Highlighter searchWords={[query]} textToHighlight={item} />
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
};
