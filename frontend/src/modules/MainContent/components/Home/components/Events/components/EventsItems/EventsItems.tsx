import { FC, useContext } from 'react';

import { Event } from '@/components/Event';
import { EVENTS_GAP } from '@/constants/common';
import { Context } from '@/providers/context';
import { useAppSelector } from '@/store';

import clsx from 'clsx';
import { motion, Transition, useMotionValue } from 'motion/react';

import { ImgIndexProps } from '../../Events';

const classes = {
  events: clsx('relative mt-6'),
  wrapper: clsx('flex h-112.5 cursor-grab items-center active:cursor-grabbing'),
};

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
    <div className={classes.events}>
      <motion.div
        drag='x'
        className={classes.wrapper}
        dragConstraints={{ right: 0, left: 0 }}
        style={{ gap: EVENTS_GAP, x: dragX }}
        onDragEnd={onDragEndHandler}
        transition={transition}
        animate={{
          translateX: `calc(((${eventWidth} + ${EVENTS_GAP}) * ${imgIndex}) * -1)`,
        }}
      >
        {/* [] is temporary */}
        {[].map(event => {
          return (
            <Event
              key={event.id}
              name={event.name}
              description={event.description}
              onlineTraining={event.onlineTraining}
              intensity={event.intensity}
              photoUrls={event.photoUrls}
            />
          );
        })}
      </motion.div>
    </div>
  );
};
