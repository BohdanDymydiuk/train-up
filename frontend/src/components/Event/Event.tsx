import { FC, Fragment, useContext } from 'react';

import { ALT_STRINGS, COMMON_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';
import { Event as EventType } from '@/shared/types/events';

import Image from 'next/image';

import { Format } from '../Format';
import { SmallArrow } from '../svgs/arrows/SmallArrow';

import styles from './Event.module.css';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining' | 'photoUrls'
>;

export const Event: FC<Props> = ({
  name,
  description,
  onlineTraining,
  intensity,
  photoUrls,
}) => {
  const { eventWidth } = useContext(Context);

  return (
    <div className={styles.container} style={{ minWidth: eventWidth }}>
      <div className={styles['image-container']}>
        <Image
          src={photoUrls[0]}
          alt={ALT_STRINGS.image}
          className={styles.image}
        />
      </div>

      <div className={styles['second-part']}>
        <div className={styles.formats}>
          <Format isOnline={false} />
          {onlineTraining && (
            <Format isOnline={onlineTraining} backgroundColor={'bg-main'} />
          )}
        </div>

        <div className={styles.intensity}>
          {[...Array(3).keys()].map(num => {
            return (
              <Fragment key={num}>
                {num < intensity ? (
                  <Image
                    src='/icons/filled-thunder.svg'
                    className={styles.thunder}
                    alt={ALT_STRINGS.filledThunder}
                  />
                ) : (
                  <Image
                    src='/icons/thunder.svg'
                    className={styles.thunder}
                    alt={ALT_STRINGS.thunder}
                  />
                )}
              </Fragment>
            );
          })}
        </div>
      </div>

      <div className={styles['third-part']}>
        <div>
          <h3 className={styles.title}>{name}</h3>
          <div className={styles.description}>{description}</div>
        </div>
        <div className={styles['learn-more']}>
          {COMMON_STRINGS.learnMore} <SmallArrow />
        </div>
      </div>
    </div>
  );
};
