import { FC } from 'react';

import { ALT_STRINGS } from '@/constants/strings';

import Image from 'next/image';

import { blocks } from './constants/blocks';

import styles from './Blocks.module.scss';

export const Blocks: FC = () => {
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
              className={styles.avatar}
              src={imageSrc}
              alt={ALT_STRINGS.avatar}
            />
            <h3 className={styles.title}>{title}</h3>
            <div className={styles.description}>{description}</div>
          </div>
        );
      })}
    </div>
  );
};
