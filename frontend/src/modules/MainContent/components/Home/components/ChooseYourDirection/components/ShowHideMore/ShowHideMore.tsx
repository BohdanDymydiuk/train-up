import React from 'react';

import styles from './ShowHideMore.module.scss';

enum Values {
  show = 'Переглянути всі',
  hide = 'Приховати',
}

interface Props {
  areAllShown: boolean;
  setAreAllShown: React.Dispatch<React.SetStateAction<boolean>>;
}

export const ShowHideMore: React.FC<Props> = ({
  areAllShown,
  setAreAllShown,
}) => {
  return (
    <button
      className={styles.button}
      onClick={() => setAreAllShown(value => !value)}
    >
      {areAllShown ? Values.hide : Values.show}
    </button>
  );
};
