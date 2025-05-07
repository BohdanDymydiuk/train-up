import React from 'react';

import styles from './Img.module.scss';

export const Img: React.FC = () => {
  return (
    <div className={styles['img-wrapper']}>
      <img src='/images/bi_image.svg' alt='img' className={styles.img} />
    </div>
  );
};
