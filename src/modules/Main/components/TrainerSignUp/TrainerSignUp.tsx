import React from 'react';

import styles from './TrainerSignUp.module.scss';

export const TrainerSignUp: React.FC = () => {
  return (
    <section className={styles['t-sign-up']}>
      <div className={styles.wrapper}>
        <h2 className={styles.title}>
          Зареєструйся тренером та <br />
          заробляй разом з TrainUp
        </h2>
        <button className={styles.button}>Стати тренером</button>
      </div>
    </section>
  );
};
