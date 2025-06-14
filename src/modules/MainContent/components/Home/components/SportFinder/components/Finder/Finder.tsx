import React from 'react';

import { ChevronDownSVG } from '../../../../../../../../reusables/svgs/ChevronDownSVG';

import styles from './Finder.module.scss';

enum Texts {
  sport = 'Яким видом спорту займаєтесь?',
  city = 'Оберіть місто',
}

export const Finder: React.FC = () => {
  return (
    <div className={styles.finder}>
      {Object.values(Texts).map(item => {
        return (
          <div className={styles.select} key={item}>
            <div className={styles.text}>{item}</div>
            <ChevronDownSVG />
          </div>
        );
      })}
      <div className={styles.online}>
        <input
          className={styles.input}
          type='checkbox'
          name='online'
          id='online'
        />
        <label className={styles.label} htmlFor='online'>
          Онлайн
        </label>
      </div>
      <button className={styles.find}>Шукати</button>
    </div>
  );
};
