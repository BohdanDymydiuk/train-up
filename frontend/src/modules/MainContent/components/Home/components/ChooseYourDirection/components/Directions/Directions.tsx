import { FC, useContext, useMemo } from 'react';

import { Format } from '@/components/Format';
import { TW_TYPO_ERMILOV_24 } from '@/constants/tailwind';
import { Context } from '@/providers/context';
import { useAppSelector } from '@/store';

import clsx from 'clsx';
import Image from 'next/image';

interface Props {
  areAllShown: boolean;
}

const classes = {
  wrapper: clsx(
    'sticky mt-8 flex flex-col content-start justify-center gap-8 overflow-hidden transition-[height] duration-500 [--block-height:297px] [--gap:32px] md:mt-12 md:[--block-height:250px] xl:grid xl:grid-cols-2 xl:gap-x-10',
  ),
  block: clsx(
    'bg-surface-main flex min-h-(--block-height) flex-col justify-between rounded-2xl p-6',
  ),
  firstPart: clsx('flex flex-col md:flex-row md:justify-between'),
  formats: clsx('flex gap-3.75'),
  imageWrapper: clsx(
    'border-blue-light mt-4 flex h-25.5 w-25.5 items-center justify-center rounded-2xl border bg-white md:mt-auto',
  ),
  sportName: clsx(TW_TYPO_ERMILOV_24, 'md:text-32'),
};

export const Directions: FC<Props> = ({ areAllShown }) => {
  const { onDesktop } = useContext(Context);

  const trainers = useAppSelector(state => state.trainers);
  const sports = useAppSelector(state => state.sports);

  const sportsShownNumber = useMemo(() => {
    return [...sports].slice(0, areAllShown ? sports.length : 6).length;
  }, [areAllShown, sports]);

  const rows = onDesktop ? Math.ceil(sportsShownNumber / 2) : sportsShownNumber;

  const wrapperHeight = `calc((var(--block-height) * ${rows}) + (var(--gap) * ${rows - 1}))`;

  return (
    <div className={classes.wrapper} style={{ height: wrapperHeight }}>
      {sports.map(sport => {
        const { id, sportName, sportIconUrl } = sport;

        const onlineCondition = trainers.some(
          ({ sportIds, onlineTraining }) =>
            sportIds.includes(id) && onlineTraining,
        );

        return (
          <div className={classes.block} key={id}>
            <div className={classes.firstPart}>
              <div className={classes.formats}>
                <Format isOnline={false} />
                {onlineCondition && <Format isOnline={true} />}
              </div>
              <div className={classes.imageWrapper}>
                {sportIconUrl && (
                  <Image width={56} height={56} alt='' src={sportIconUrl} />
                )}
              </div>
            </div>

            <h3 className={classes.sportName}>{sportName}</h3>
          </div>
        );
      })}
    </div>
  );
};
