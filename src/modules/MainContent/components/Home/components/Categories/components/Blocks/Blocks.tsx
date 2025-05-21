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
      {categories.map(item => {
        return (
          <div className={styles.block} key={item}>
            {item}
          </div>
        );
      })}
    </div>
  );
};
