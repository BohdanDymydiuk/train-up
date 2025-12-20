import { FC, useContext } from 'react';

import { EVENTS_GAP } from '@/constants/common';
import { Context } from '@/providers/context';
import { Event } from '@/components/Event';
import { useAppSelector } from '@/store';

import { motion, Transition, useMotionValue } from 'motion/react';

import { ImgIndexProps } from '../../Events';

import styles from './EventsItems.module.scss';

const DRAG_BUFFER = 50;

export const EventsItems: FC<ImgIndexProps> = ({ imgIndex, setImgIndex }) => {
  const events = useAppSelector(state => state.events);
  const { eventWidth } = useContext(Context);

  const dragX = useMotionValue(0);

  const onDragEndHandler = () => {
    const x = dragX.get();

    if (x <= -DRAG_BUFFER && imgIndex < events.length - 1) {
      setImgIndex(prev => prev + 1);
    }

    if (x >= DRAG_BUFFER && imgIndex > 0) {
      setImgIndex(prev => prev - 1);
    }
  };

  const transition: Transition = {
    duration: 0.5,
    ease: 'linear',
  };

  return (
    <div className={styles.events}>
      <motion.div
        drag='x'
        className={styles.wrapper}
        dragConstraints={{ right: 0, left: 0 }}
        style={{ gap: EVENTS_GAP, x: dragX }}
        onDragEnd={onDragEndHandler}
        transition={transition}
        animate={{
          translateX: `calc(((${eventWidth} + ${EVENTS_GAP}) * ${imgIndex}) * -1)`,
        }}
      >
        {[].map(event => {
          const {
            id,
            name,
            description,
            onlineTraining,
            intensity,
            photoUrls,
          } = event;

          return (
            <Event
              key={id}
              {...{
                name,
                description,
                onlineTraining,
                intensity,
                photoUrls,
              }}
            />
          );
        })}
      </motion.div>
    </div>
  );
};
