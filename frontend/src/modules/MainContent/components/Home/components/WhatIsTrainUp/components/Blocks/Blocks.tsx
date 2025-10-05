import React from 'react';

import Image from 'next/image';

import { blocks } from './constants/blocks';

import styles from './Blocks.module.scss';

export const Blocks: React.FC = () => {
  return (
    <div className={styles.blocks}>
      {blocks.map(block => {
        const { imageSrc, title, description } = block;

        return (
          <div className={styles.block} key={title}>
            <Image
              width={0}
              height={0}
              sizes='100vw'
              style={{ width: 'auto', height: '94px', alignSelf: 'end' }}
              src={imageSrc}
              alt='avatar'
            />
            <h3 className={styles.title}>{title}</h3>
            <div className={styles.description}>{description}</div>
          </div>
        );
      })}
    </div>
  );
};
