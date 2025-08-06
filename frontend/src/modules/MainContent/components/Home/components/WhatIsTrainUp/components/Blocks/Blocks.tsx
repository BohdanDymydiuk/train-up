import React from 'react';

import { blocks } from './constants/blocks';

import styles from './Blocks.module.scss';

export const Blocks: React.FC = () => {
  return (
    <div className={styles.blocks}>
      {blocks.map(block => {
        const { imageSrc, title, description } = block;

        return (
          <div className={styles.block} key={title}>
            <img className={styles.img} src={imageSrc} alt='avatar' />
            <h3 className={styles.title}>{title}</h3>
            <div className={styles.description}>{description}</div>
          </div>
        );
      })}
    </div>
  );
};
