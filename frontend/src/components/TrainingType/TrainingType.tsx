import { CSSProperties, FC } from 'react';

import { Formats } from '@/shared/enums';

import styles from './TrainingType.module.scss';

interface Props {
  type: string;
}

export const TrainingType: FC<Props> = ({ type }) => {
  const offlineCssProps: CSSProperties = {
    border: `1px solid ${styles.brownColor}`,
  };

  const onlineCssProps: CSSProperties = {
    border: `1px solid ${styles.darkColor}`,
    backgroundColor: styles.darkColor,
    color: '#F4DCDC',
  };

  const typeStyle = type === Formats.online ? onlineCssProps : offlineCssProps;

  return (
    <div className={styles.type} style={typeStyle}>
      {type}
    </div>
  );
};
