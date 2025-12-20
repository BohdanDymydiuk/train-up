import { CSSProperties, FC, useContext } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { FacebookSVG } from '@/components/svgs/socials/FacebookSVG';
import { InstagramSVG } from '@/components/svgs/socials/InstagramSVG';
import { ThreadsSVG } from '@/components/svgs/socials/ThreadsSVG';
import { FOOTER_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';
import { NavItems } from '@/shared/enums';

import styles from './Footer.module.scss';

export const Footer: FC = () => {
  const { onDesktop } = useContext(Context);

  const pages = Object.values(NavItems).filter(
    page => page !== NavItems.aboutUs,
  );

  const socials = {
    instagram: <InstagramSVG />,
    threads: <ThreadsSVG />,
    facebook: <FacebookSVG />,
  };

  const keys = Object.keys(socials);
  const values = Object.values(socials);

  const ermilovTitleProps = {
    title: FOOTER_STRINGS.title,
    cssProps: { fontSize: onDesktop && '40px' } as CSSProperties,
  };

  return (
    <footer className={styles.footer}>
      <div className={styles['content-wrapper']}>
        <div className={styles['title-wrapper']}>
          <ErmilovTitle {...ermilovTitleProps} />
        </div>

        <div className={styles['pages-socials-wrapper']}>
          <div className={styles['pages-wrapper']}>
            <div className={styles['pages-text']}>{FOOTER_STRINGS.pages}</div>

            <nav className={styles.pages}>
              <ul>
                {pages.map(page => {
                  return (
                    <li className={styles.page} key={page}>
                      {page}
                    </li>
                  );
                })}
              </ul>
            </nav>
          </div>

          <div className={styles['socials-wrapper']}>
            <div className={styles['socials-text']}>
              {FOOTER_STRINGS.socials}
            </div>

            <div className={styles.socials}>
              {values.map((value, index) => {
                const key = keys[index];

                return (
                  <button className={styles.social} key={key}>
                    {value}
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};
