import React from 'react';

import styles from './Blocks.module.scss';

export const Blocks: React.FC = () => {
  const categories: string[] = [
    'Gym',
    'TRX',
    'Swimming',
    'Crossfit',
    'Squash',
    'Box',
    'Stretching',
    'Yoga',
    'Pilates',
    'Kids fitness',
    'Kids swimming',
    "Women's boxing",
    'Ping pong',
    'Aqua aerobics',
    'Cycling',
  ];

  return (
    <div className={styles.blocks}>
      {categories.map((item, index) => {
        return (
          <div className={styles.block} key={`category-${index}`}>
            {item}
          </div>
        );
      })}
    </div>
  );
};
