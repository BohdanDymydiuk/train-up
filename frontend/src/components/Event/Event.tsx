import { FC, Fragment, useContext } from 'react';

import { ALT_STRINGS, COMMON_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';
import { Event as EventType } from '@/shared/types/events';

import clsx from 'clsx';
import Image from 'next/image';

import { Format } from '../Format';
import { SmallArrow } from '../svgs/arrows/SmallArrow';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining' | 'photoUrls'
>;

const classes = {
  container: clsx('border-ui-gray-400 h-full rounded-2xl border bg-white p-6'),
  imageContainer: clsx('h-47 overflow-hidden rounded-2xl'),
  image: clsx('pointer-events-none h-full w-full object-cover leading-[200%]'),
  secondPart: clsx('mt-6 flex justify-between'),
  formats: clsx('flex gap-3.75'),
  intensity: clsx('flex h-8 items-center gap-1.75'),
  thunder: clsx('h-6 w-4.75'),
  thirdPart: clsx('mt-6 flex h-33.25 flex-col justify-between'),
  title: clsx('font-[WF_Visual_Sans] font-medium'),
  description: clsx('mt-1.5 font-[Inter] text-sm font-normal'),
};

export const Event: FC<Props> = ({
  name,
  description,
  onlineTraining,
  intensity,
  photoUrls,
}) => {
  const { eventWidth } = useContext(Context);

  return (
    <div className={classes.container} style={{ minWidth: eventWidth }}>
      <div className={classes.imageContainer}>
        <Image
          src={photoUrls[0]}
          alt={ALT_STRINGS.image}
          className={classes.image}
        />
      </div>

      <div className={classes.secondPart}>
        <div className={classes.formats}>
          <Format isOnline={false} />
          {onlineTraining && (
            <Format isOnline={onlineTraining} backgroundColor={'bg-main'} />
          )}
        </div>

        <div className={classes.intensity}>
          {[...Array(3).keys()].map(num => {
            return (
              <Fragment key={num}>
                {num < intensity ? (
                  <Image
                    src='/icons/filled-thunder.svg'
                    className={classes.thunder}
                    alt={ALT_STRINGS.filledThunder}
                  />
                ) : (
                  <Image
                    src='/icons/thunder.svg'
                    className={classes.thunder}
                    alt={ALT_STRINGS.thunder}
                  />
                )}
              </Fragment>
            );
          })}
        </div>
      </div>

      <div className={classes.thirdPart}>
        <div>
          <h3 className={classes.title}>{name}</h3>
          <div className={classes.description}>{description}</div>
        </div>
        <div>
          {COMMON_STRINGS.learnMore} <SmallArrow />
        </div>
      </div>
    </div>
  );
};
