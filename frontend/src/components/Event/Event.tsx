import { CSSProperties, FC, Fragment, useContext } from 'react';

import { ALT_STRINGS, COMMON_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';
import { Event as EventType } from '@/shared/types/events';

import Image from 'next/image';

import { Format } from '../Format';
import { SmallArrow } from '../svgs/arrows/SmallArrow';

import styles from './Event.module.scss';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining' | 'photoUrls'
>;

const secondPartCssProps: CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  marginTop: '24px',
};

const thirdPartCssProps: CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  height: '133px',
  marginTop: '24px',
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
    <div className={styles.event} style={{ minWidth: eventWidth }}>
      <div className={styles['img-wrapper']}>
        <Image src={photoUrls[0]} alt={ALT_STRINGS.image} />
      </div>

      <div style={secondPartCssProps}>
        <div className={styles.formats}>
          <Format isOnline={false} />
          {onlineTraining && (
            <Format isOnline={onlineTraining} backgroundColor={styles.white} />
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

      <div style={thirdPartCssProps}>
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
