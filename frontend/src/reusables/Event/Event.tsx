import React, { useContext } from 'react';

import { MainContext } from '../../context/MainContext';
import { Event as EventType } from '../../types/Event';

import styles from './Event.module.scss';

type Props = Pick<
  EventType,
  'name' | 'description' | 'intensity' | 'onlineTraining'
>;

export const Event: React.FC<Props> = () => {
  const { eventWidth } = useContext(MainContext);

  return (
    <div className={styles.event} style={{ minWidth: eventWidth }}>
      Lorem ipsum dolor sit amet consectetur adipisicing elit. Beatae, vel? Iure
      placeat omnis odio, blanditiis facilis a harum! Nesciunt voluptatibus
      culpa placeat impedit illum numquam, facere commodi repellendus molestiae
      cum quo et sed. Itaque laboriosam aut saepe rerum consequuntur voluptates
      fugit quas mollitia, maiores reprehenderit reiciendis magni voluptatibus?
      Blanditiis inventore, quibusdam at libero recusandae mollitia quo dolorem
      praesentium amet aperiam fugiat pariatur facere in debitis non rem
      provident, explicabo vitae earum expedita itaque? Id voluptas laborum illo
      ratione, officia voluptatum rem quidem suscipit culpa harum error
      doloremque laboriosam reiciendis praesentium optio modi vero nisi non!
      Blanditiis illum incidunt veritatis sequi!
    </div>
  );
};
