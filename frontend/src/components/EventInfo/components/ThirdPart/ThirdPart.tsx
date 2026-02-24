import { FC, useMemo } from 'react';

import { EventInfoType } from '@/shared/types/events';

import clsx from 'clsx';

type Props = Pick<EventInfoType, 'name' | 'description'>;

const classes = {
  root: clsx('mt-9.5'),
  title: clsx('font-[Inter] text-xl font-semibold'),
  descr: clsx('font-[Inter] text-sm font-normal'),
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
