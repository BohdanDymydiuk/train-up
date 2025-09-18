import React, { useContext } from 'react';

import { MainContext } from '../../context/MainContext';
import { Event as EventType } from '../../types/Event';
import { Format } from '../Format';
import { SmallArrow } from '../svgs/arrows/SmallArrow';

import styles from './Event.module.scss';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining' | 'photoUrls'
>;

const secondPartCssProps: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  marginTop: '24px',
};

const thirdPartCssProps: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
  height: '133px',
  marginTop: '24px',
};

export const Event: React.FC<Props> = ({
  name,
  description,
  onlineTraining,
  intensity,
  photoUrls,
}) => {
  const { eventWidth } = useContext(MainContext);

  return (
    <div className={styles.event} style={{ minWidth: eventWidth }}>
      <div className={styles['img-wrapper']}>
        <img src={photoUrls[0]} alt='Image' />
      </div>

      <div style={secondPartCssProps}>
        <div className={styles.formats}>
          <Format isOnline={false} />
          {onlineTraining && (
            <Format isOnline={onlineTraining} backgroundColor={styles.gray} />
          )}
        </div>

        <div className={styles.intensity}>
          {[...Array(3).keys()].map(num => {
            return (
              <img
                key={num}
                src={`../../../public/icons/${num < intensity ? 'filled-thunder' : 'thunder'}.svg`}
                style={{ width: '19px', height: '24px' }}
                alt='thunder'
              />
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
          Дізнатись більше <SmallArrow />
        </div>
      </div>
    </div>
  );
};
