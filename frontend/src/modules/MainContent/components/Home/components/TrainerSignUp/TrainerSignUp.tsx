import React, { useContext } from 'react';

import { MainContext } from '../../../../../../context/MainContext';
import { ErmilovTitle } from '../../../../../../reusables/ErmilovTitle';

import styles from './TrainerSignUp.module.scss';

export const TrainerSignUp: React.FC = () => {
  const { onTablet, onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: (
      <>
        Зареєструйся тренером та <br />
        заробляй разом з <span style={{ color: styles.orange }}>TrainUp</span>
      </>
    ),
    cssProps: {
      fontSize: (onDesktop && '40px') || (onTablet && '32px'),
    } as React.CSSProperties,
  };

  return (
    <section className={styles['sign-up']}>
      <div className={styles.wrapper}>
        <div className={styles['first-part']}>
          <ErmilovTitle {...ermilovTitleProps} />
          <button className={styles.button}>Стати тренером</button>
        </div>
        {onTablet && (
          <div className={styles['second-part']}>
            <img
              className={styles.avatar}
              src='/images/avatars/avatar_4.png'
              alt='avatar'
            />
          </div>
        )}
      </div>
    </section>
  );
};
