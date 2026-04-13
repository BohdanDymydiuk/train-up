import { FC } from 'react';

import { ALT_STRINGS, AUTH_STRINGS } from '@/constants/strings';
import { TW_TYPO_INTER_600_30 } from '@/constants/tailwind';

import clsx from 'clsx';
import Image from 'next/image';

const classes = {
  modalWrapper: clsx(
    'fixed inset-0 flex items-center justify-center bg-black/30',
  ),
  modal: clsx('relative w-[min(100%,480px)] rounded-[20px] bg-white p-7.5'),
  cross: clsx(
    'absolute top-7.5 right-7.5 h-3.75 w-3.75 transition-[transform] duration-200 hover:scale-120 active:scale-130',
  ),
  title: clsx(TW_TYPO_INTER_600_30, 'text-center'),
};

interface Props {
  setIsModalShown: (value: boolean) => void;
}

export const SignUpModal: FC<Props> = ({ setIsModalShown }) => {
  const onClickHandler = () => setIsModalShown(false);

  return (
    <div className={classes.modalWrapper}>
      <div className={classes.modal}>
        <Image
          src='/icons/cross.svg'
          alt={ALT_STRINGS.cross}
          className={classes.cross}
          onClick={onClickHandler}
        />
        <h3 className={classes.title}>{AUTH_STRINGS.signUpTitle}</h3>
      </div>
    </div>
  );
};
