import React, { useContext } from 'react';

import { ALT_STRINGS, TRAINER_SIGNUP_STRINGS } from '@/constants/strings';
import { MainContext } from '@/context/MainContext';
import { ErmilovTitle } from '@/reusables/ErmilovTitle';

import Image from 'next/image';

import styles from './TrainerSignUp.module.scss';

export const TrainerSignUp: React.FC = () => {
  const { onTablet, onDesktop } = useContext(MainContext);

  const ermilovTitleProps = {
    title: (
      <>
        {TRAINER_SIGNUP_STRINGS.title.firstPart} <br />
        {TRAINER_SIGNUP_STRINGS.title.secondPart}{' '}
        <span style={{ color: styles.orange }}>
          {TRAINER_SIGNUP_STRINGS.appName}
        </span>
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
          <button className={styles.button}>
            {TRAINER_SIGNUP_STRINGS.button}
          </button>
        </div>
        {onTablet && (
          <div className={styles['second-part']}>
            <Image
              width={0}
              height={0}
              sizes='100vw'
              className={styles.avatar}
              src='/images/avatars/avatar_4.png'
              alt={ALT_STRINGS.avatar}
            />
          </div>
        )}
      </div>
    </section>
  );
};
