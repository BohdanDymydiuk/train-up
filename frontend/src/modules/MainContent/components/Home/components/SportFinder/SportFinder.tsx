import { CSSProperties, FC, useContext, useEffect, useState } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { REGIONAL_CENTERS } from '@/constants/regionalCenters';
import { SPORT_FINDER_STRINGS } from '@/constants/strings';
import { TW_BUTTON_BASE } from '@/constants/tailwind';
import { DropdownHoc } from '@/hocs/DropdownHoc';
import { Context } from '@/providers/context';
import { FinderTexts } from '@/shared/enums';
import { useAppSelector } from '@/store';

import clsx from 'clsx';

import { Button } from './components/Button';
import { Dropdown } from './components/Dropdown';

import styles from './SportFinder.module.css';

const classes = {
  wrapper: clsx(
    'bg-blue-light mt-5.5 rounded-4xl px-4 py-14.5',
    'md:mt-22.5 md:px-12 md:py-19.5',
    'xl:mt-18 xl:py-22.5',
    '2xl:flex 2xl:flex-col 2xl:items-center',
  ),
  find: clsx(TW_BUTTON_BASE, styles.find),
};

// Some styles stay in CSS where it results in clearer and more maintainable code.

const ChooseSport = DropdownHoc(Button, Dropdown);
const ChooseCity = DropdownHoc(Button, Dropdown);

export const SportFinder: FC = () => {
  const { onTablet, onSmallDesktop } = useContext(Context);

  const sports = useAppSelector(state => state.sports);

  const sportsNames = sports.map(sport => sport.sportName);

  const [isMounted, setIsMounted] = useState(false);

  const [isOnline, setIsOnline] = useState(false);
  const [selectedSport, setSelectedSport] = useState('');
  const [selectedCity, setSelectedCity] = useState('');

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  // #region props
  const ermilovTitleProps = {
    title: (
      <>
        {SPORT_FINDER_STRINGS.title.firstPart} {onTablet && <br />}{' '}
        {SPORT_FINDER_STRINGS.title.secondPart}
      </>
    ),
    cssProps: { textAlign: 'center' } as CSSProperties,
  };

  const chooseSportProps = {
    text: selectedSport || FinderTexts.sport,
    select: setSelectedSport,
    items: sportsNames,
  };

  const chooseCityProps = {
    text: selectedCity || FinderTexts.city,
    select: setSelectedCity,
    items: Object.values(REGIONAL_CENTERS),
    isOnline,
  };
  // #endregion

  return (
    <div className={classes.wrapper}>
      <ErmilovTitle {...ermilovTitleProps} />
      <div className={styles.finder}>
        <div
          className={styles['inputs-wrapper']}
          style={{ display: !onSmallDesktop ? 'contents' : 'flex' }}
        >
          <ChooseSport {...chooseSportProps} />
          <ChooseCity {...chooseCityProps} />

          <div className={styles.online}>
            <input
              className={styles.checkbox}
              type='checkbox'
              name='online'
              id='online'
              checked={isOnline}
              onChange={event => setIsOnline(event.target.checked)}
            />
            <label className={styles.label} htmlFor='online'>
              {SPORT_FINDER_STRINGS.online}
            </label>
          </div>
        </div>
        <button className={classes.find}>{SPORT_FINDER_STRINGS.search}</button>
      </div>
    </div>
  );
};
