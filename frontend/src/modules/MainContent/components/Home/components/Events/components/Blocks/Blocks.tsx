import React, { useMemo } from 'react';

import styles from './Blocks.module.scss';

export const Blocks: React.FC = () => {
  const descriptions = useMemo(() => {
    const arr = [];

    for (let i = 0; i < 3; i++) {
      arr.push('Відкриття найбільшого Premium спортивного клубу');
    }

    return arr;
  }, []);

  return (
    <div className={styles.blocks}>
      {descriptions.map((descr, index) => {
        return (
          <div className={styles.block} key={index}>
            <div className={styles['img-wrapper']}>
              <img src='/images/bi_image.svg' alt='' />
            </div>
            <div className={styles.descr}>{descr}</div>
          </div>
        );
      })}
    </div>
  );
};
