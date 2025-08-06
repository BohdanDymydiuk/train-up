import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';
import { ChevronDownSVG } from '../../../../../../reusables/svgs/ChevronDownSVG';

import styles from './SportFinder.module.scss';

enum Texts {
  sport = 'Яким видом спорту займаєтесь?',
  city = 'Оберіть місто',
}

export const SportFinder: React.FC = () => {
  const { onTablet, onSmallDesktop } = useContext(MainContext);

  const chevronDownSvgCssProps = {
    svgStyle: { minWidth: '16px' },
    pathStyle: { fill: `${styles.gray}` },
  };

  const ermilovTitleProps = {
    title: (
      <>
        Твій спорт — твій вибір. {onTablet && <br />} Ми поруч на кожному етапі
        шляху
      </>
    ),
    CssProps: { textAlign: 'center' } as React.CSSProperties,
  };

  return (
    <div className={styles.wrapper}>
      <ErmilovTitle {...ermilovTitleProps} />
      <div className={styles.finder}>
        <div
          className={styles['inputs-wrapper']}
          style={{ display: !onSmallDesktop ? 'contents' : 'flex' }}
        >
          {Object.values(Texts).map(item => {
            return (
              <div className={styles.select} key={item}>
                <div className={styles.text}>{item}</div>
                <ChevronDownSVG {...chevronDownSvgCssProps} />
              </div>
            );
          })}
          <div className={styles.online}>
            <input
              className={styles.checkbox}
              type='checkbox'
              name='online'
              id='online'
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
