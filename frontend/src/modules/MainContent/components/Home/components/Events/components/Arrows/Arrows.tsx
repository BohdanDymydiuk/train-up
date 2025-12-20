import { FC } from 'react';

import { ArrowLeft } from '@/components/svgs/arrows/ArrowLeft';
import { ArrowRight } from '@/components/svgs/arrows/ArrowRight';
import { useAppSelector } from '@/store';

import { ImgIndexProps } from '../../Events';

import styles from './Arrows.module.scss';

const arrows = {
  left: <ArrowLeft />,
  rigth: <ArrowRight />,
};

const keys = Object.keys(arrows);
const values = Object.values(arrows);

export const Arrows: FC<ImgIndexProps> = ({ imgIndex, setImgIndex }) => {
  const events = useAppSelector(state => state.events);

  const buttonOnClickHandlers = [
    () => {
      if (imgIndex > 0) setImgIndex(prev => prev - 1);
    },
    () => {
      if (imgIndex < events.length - 1) setImgIndex(prev => prev + 1);
    },
  ];

  return (
    <div className={styles.arrows}>
      {keys.map((key, index) => {
        const svg = values[index];
        const onClickHandler = buttonOnClickHandlers[index];

        return (
          <button className={styles.button} key={key} onClick={onClickHandler}>
            {svg}
          </button>
        );
      })}
    </div>
  );
};
