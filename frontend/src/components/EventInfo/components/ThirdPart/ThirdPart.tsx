import { FC, useMemo } from 'react';

import {
  TW_TYPO_INTER_400_14,
  TW_TYPO_INTER_600_20,
} from '@/constants/tailwind';
import { EventInfoType } from '@/shared/types/events';

import clsx from 'clsx';

type Props = Pick<EventInfoType, 'name' | 'description'>;

const classes = {
  root: clsx('mt-9.5'),
  title: TW_TYPO_INTER_600_20,
  descr: TW_TYPO_INTER_400_14,
};

export const ThirdPart: FC<Props> = ({ name, description }) => {
  const descr = useMemo(() => {
    const dotIndex = [...description].findIndex(ch => ch === '.');
    const firstPart = description.slice(0, dotIndex + 1);
    const secondPart = description.slice(dotIndex + 1);

    return (
      <>
        {firstPart}
        <br />
        {secondPart}
      </>
    );
  }, []);

  return (
    <div className={classes.root}>
      <h3 className={classes.title}>{name}</h3>
      <div className={classes.descr}>{descr}</div>
    </div>
  );
};
