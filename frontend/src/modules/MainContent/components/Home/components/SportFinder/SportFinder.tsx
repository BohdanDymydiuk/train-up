import React, { useContext, useState } from 'react';

import { REGIONAL_CENTERS } from '@/constants/regionalCenters';
import { MainContext } from '@/context/MainContext';
import { FinderTexts } from '@/enums/FinderTexts';
import { DropdownHoc } from '@/reusables/DropdownHoc';
import { ErmilovTitle } from '@/reusables/ErmilovTitle';
import { useAppSelector } from '@/store';

import { Button } from './components/Button';
import { Dropdown } from './components/Dropdown';

import styles from './SportFinder.module.scss';

const ChooseSport = DropdownHoc(Button, Dropdown);
const ChooseCity = DropdownHoc(Button, Dropdown);

export const SportFinder: React.FC = () => {
  const { onTablet, onSmallDesktop } = useContext(MainContext);

  const sports = useAppSelector(state => state.sports);

  const sportsNames = sports.map(sport => sport.sportName);

  const [isOnline, setIsOnline] = useState(false);
  const [selectedSport, setSelectedSport] = useState('');
  const [selectedCity, setSelectedCity] = useState('');

  // #region props
  const ermilovTitleProps = {
    title: (
      <>
        Твій спорт — твій вибір. {onTablet && <br />} Ми поруч на кожному етапі
        шляху
      </>
    ),
    cssProps: { textAlign: 'center' } as React.CSSProperties,
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
    <div className={styles.wrapper}>
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
              Онлайн
            </label>
          </div>
        </div>
        <button className={styles.find}>Шукати</button>
      </div>
    </div>
  );
};
