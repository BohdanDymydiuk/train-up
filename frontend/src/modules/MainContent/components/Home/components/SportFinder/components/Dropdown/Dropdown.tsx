import { CSSProperties, FC, useContext, useMemo, useState } from 'react';
import Highlighter from 'react-highlight-words';

import { SearchSVG } from '@/components/svgs/SearchSVG';
import { APPEARING_DP_CSS_PROPS } from '@/constants/common';
import { SPORT_FINDER_STRINGS } from '@/constants/strings';
import { TW_TYPO_INTER_500_15 } from '@/constants/tailwind';
import { DropdownProps } from '@/hocs/DropdownHoc';
import { Context } from '@/providers/context';
import { FinderTexts } from '@/shared/enums';

import clsx from 'clsx';

const classes = {
  dropdown: clsx(
    'border-ui-gray-400 absolute top-[calc(var(--finder-row-height)+8px)] z-1 origin-top scale-y-0 overflow-hidden rounded-lg border bg-white p-6 opacity-0 transition-[opacity,transform] xl:top-[calc(var(--finder-row-height)+32px)]',
  ),
  searchWrapper: clsx(
    'flex h-12 items-center justify-center gap-2.25 rounded-lg border border-gray-200 p-4 [&_svg]:h-4.75 [&_svg]:w-4.75',
  ),
  search: clsx(TW_TYPO_INTER_500_15, 'w-full border-none outline-none'),
  dpList: clsx(
    TW_TYPO_INTER_500_15,
    'mt-2 flex max-h-58 list-none flex-col gap-2 overflow-y-auto',
  ),
  dpItem: clsx('hover:bg-surface-main cursor-pointer p-4'),
};

const FINDER_PADDING = '24px';

export const Dropdown: FC<DropdownProps> = ({
  isDpShown,
  closeDpHandler,
  items,
  select,
  text,
}) => {
  const { onTablet, onDesktop } = useContext(Context);

  const [query, setSquery] = useState('');
  const [placeholder, setPlaceholder] = useState<string>(
    SPORT_FINDER_STRINGS.searchPlaceholder,
  );

  const onClickInputHandler = () => setPlaceholder('');

  const fileredItems = useMemo(() => {
    return items?.filter(item =>
      item.toLowerCase().includes(query.toLowerCase()),
    );
  }, [query]);

  const dpCssProps: CSSProperties = useMemo(() => {
    const cityCondition = text === FinderTexts.city;
    const x2Condition = !onTablet || (onTablet && cityCondition);

    const result: CSSProperties = {
      width: `calc(100% + ${FINDER_PADDING}${x2Condition ? ' * 2' : ''})`,
      left: `calc(${FINDER_PADDING} * -1)`,
    };

    if (onDesktop && cityCondition) result.width = '125%';

    return result;
  }, [onTablet, onDesktop]);

  const dpStyle = isDpShown
    ? { ...APPEARING_DP_CSS_PROPS, ...dpCssProps }
    : dpCssProps;

  const onClickHandler = (name: string) => {
    if (closeDpHandler) closeDpHandler();
    if (select) setTimeout(() => select(name), 100);
  };

  return (
    <div className={classes.dropdown} style={dpStyle}>
      <div className={classes.searchWrapper}>
        <SearchSVG />

        <input
          className={classes.search}
          type='text'
          value={query}
          onChange={event => setSquery(event.target.value)}
          onClick={onClickInputHandler}
          placeholder={placeholder}
        />
      </div>
      {fileredItems && fileredItems.length > 0 && (
        <ul className={classes.dpList}>
          {fileredItems.map(item => {
            return (
              <li
                key={item}
                className={classes.dpItem}
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
