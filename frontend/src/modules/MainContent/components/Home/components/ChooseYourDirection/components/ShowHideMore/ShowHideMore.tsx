import { Dispatch, FC, SetStateAction } from 'react';

import { DIRECTION_STRINGS } from '@/constants/strings';

import styles from './ShowHideMore.module.scss';

interface Props {
  areAllShown: boolean;
  setAreAllShown: Dispatch<SetStateAction<boolean>>;
}

export const ShowHideMore: FC<Props> = ({ areAllShown, setAreAllShown }) => {
  return (
    <button
      className={styles.button}
      onClick={() => setAreAllShown(value => !value)}
    >
      {areAllShown ? DIRECTION_STRINGS.hide : DIRECTION_STRINGS.showAll}
    </button>
  );
};
