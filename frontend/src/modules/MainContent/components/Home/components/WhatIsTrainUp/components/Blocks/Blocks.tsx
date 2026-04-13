import { FC } from 'react';

import { ALT_STRINGS } from '@/constants/strings';
import { TW_TYPO_ERMILOV_24, TW_TYPO_INTER_400_14 } from '@/constants/tailwind';

import clsx from 'clsx';
import Image from 'next/image';

import { blocks } from './constants/blocks';

const classes = {
  blocks: clsx(
    'mt-6 flex flex-wrap gap-10 sm:justify-center md:flex-nowrap md:justify-normal',
  ),
  block: clsx(
    'border-ui-gray-400 flex flex-col rounded-2xl border bg-white p-6 sm:w-52 md:w-full',
  ),
  avatar: clsx('h-23.5 w-auto self-end'),
  title: clsx(TW_TYPO_ERMILOV_24, '3md:text-32 md:mt-4'),
  description: clsx(TW_TYPO_INTER_400_14, 'mt-4 leading-[150%] md:text-base'),
};

export const Blocks: FC = () => {
  return (
    <div className={classes.blocks}>
      {blocks.map(block => {
        const { imageSrc, title, description } = block;

        return (
          <div className={classes.block} key={title}>
            <Image
              width={0}
              height={0}
              sizes='100vw'
              className={classes.avatar}
              src={imageSrc}
              alt={ALT_STRINGS.avatar}
            />
            <h3 className={classes.title}>{title}</h3>
            <div className={classes.description}>{description}</div>
          </div>
        );
      })}
    </div>
  );
};
