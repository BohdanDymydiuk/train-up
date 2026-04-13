import { CSSProperties, FC, useContext, useEffect, useState } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { ALT_STRINGS, TRAINER_SIGNUP_STRINGS } from '@/constants/strings';
import { TW_BUTTON_BASE } from '@/constants/tailwind';
import { Context } from '@/providers/context';

import clsx from 'clsx';
import Image from 'next/image';

const classes = {
  signUp: clsx(
    'bg-blue-light mt-8 flex h-82.5 items-center rounded-4xl p-8 md:mt-16 md:h-100 xl:mt-18',
  ),
  wrapper: clsx('flex w-full items-center justify-between'),
  button: clsx(TW_BUTTON_BASE, 'mt-10 h-12 w-47.5'),
  secondPart: clsx(
    'bg-surface-main border-blue-soft flex h-30 w-30 justify-center rounded-3xl border py-3.5 xl:h-60.75 xl:w-60.75 xl:py-4',
  ),
  avatar: clsx('h-full w-auto'),
};

export const TrainerSignUp: FC = () => {
  const { onTablet, onDesktop } = useContext(Context);

  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  const ermilovTitleProps = {
    title: (
      <>
        {TRAINER_SIGNUP_STRINGS.title.firstPart} <br />
        {TRAINER_SIGNUP_STRINGS.title.secondPart}{' '}
        <span style={{ color: 'var(--color-accent)' }}>
          {TRAINER_SIGNUP_STRINGS.appName}
        </span>
      </>
    ),
    cssProps: {
      fontSize: (onDesktop && '40px') || (onTablet && '32px'),
    } as CSSProperties,
  };

  return (
    <section className={classes.signUp}>
      <div className={classes.wrapper}>
        {/* First Part */}
        <div>
          <ErmilovTitle {...ermilovTitleProps} />
          <button className={classes.button}>
            {TRAINER_SIGNUP_STRINGS.button}
          </button>
        </div>
        {onTablet && (
          <div className={classes.secondPart}>
            <Image
              width={0}
              height={0}
              sizes='100vw'
              className={classes.avatar}
              src='/images/avatars/avatar_4.png'
              alt={ALT_STRINGS.avatar}
            />
          </div>
        )}
      </div>
    </section>
  );
};
