import { CSSProperties, FC, useContext } from 'react';

import { ErmilovTitle } from '@/components/ErmilovTitle';
import { FacebookSVG } from '@/components/svgs/socials/FacebookSVG';
import { InstagramSVG } from '@/components/svgs/socials/InstagramSVG';
import { ThreadsSVG } from '@/components/svgs/socials/ThreadsSVG';
import { FOOTER_STRINGS } from '@/constants/strings';
import { Context } from '@/providers/context';
import { NavItems } from '@/shared/enums';

import clsx from 'clsx';

const classes = {
  footer: clsx('border-ui-gray-400 mt-8 border-t bg-white p-[48px_48px_54px]'),
  contentWrapper: clsx('xl:flex xl:justify-between'),
  titleWrapper: clsx('w-48.25 sm:w-90.75 md:w-150.25'),
  pagesSocialsWrapper: clsx('mt-8 md:mt-20 md:flex md:gap-9.5 xl:mt-0'),
  pages: clsx('mt-6 w-48'),
  pagesList: clsx('flex list-none flex-col gap-4'),
  page: clsx('cursor-pointer font-[Inter] text-base font-normal'),
  socialsWrapper: clsx('mt-8 md:mt-0 md:w-80.5'),
  socials: clsx('mt-6.5 flex gap-8.25'),
  social: clsx(
    'cursor-pointer border-0 bg-transparent [&_svg]:h-7.5 [&_svg]:w-7.5',
  ),
  text: clsx('font-[Inter] text-xl font-semibold'),
};

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
    <footer className={classes.footer}>
      <div className={classes.contentWrapper}>
        <div className={classes.titleWrapper}>
          <ErmilovTitle {...ermilovTitleProps} />
        </div>

        <div className={classes.pagesSocialsWrapper}>
          <div>
            <div className={classes.text}>{FOOTER_STRINGS.pages}</div>

            <nav className={classes.pages}>
              <ul className={classes.pagesList}>
                {pages.map(page => {
                  return (
                    <li className={classes.page} key={page}>
                      {page}
                    </li>
                  );
                })}
              </ul>
            </nav>
          </div>

          <div className={classes.socialsWrapper}>
            <div className={classes.text}>{FOOTER_STRINGS.socials}</div>

            <div className={classes.socials}>
              {values.map((value, index) => {
                const key = keys[index];

                return (
                  <button className={classes.social} key={key}>
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
