import React from 'react';

import { AppleSVG } from '../../../../../../../../reusables/svgs/formSvgs/AppleSVG';
import { GoogleSVG } from '../../../../../../../../reusables/svgs/formSvgs/GoogleSVG';

import styles from './Buttons.module.scss';

export const Buttons: React.FC = () => {
  return (
    <>
      <button type='submit' className={styles.signin}>
        Log in
      </button>
      <div className={styles.or}>or</div>
      <div className={styles.wrapper}>
        <button type='submit' className={styles.button}>
          <AppleSVG />
          Apple
        </button>
        <button type='submit' className={styles.button}>
          <GoogleSVG />
          Google
        </button>
      </div>
    </>
  );
};
