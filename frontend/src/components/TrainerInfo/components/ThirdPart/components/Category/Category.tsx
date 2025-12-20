import { FC } from 'react';

import styles from './Category.module.scss';

interface Props {
  category: string;
}

export const Category: FC<Props> = ({ category }) => {
  return <div className={styles.category}>{category}</div>;
};
