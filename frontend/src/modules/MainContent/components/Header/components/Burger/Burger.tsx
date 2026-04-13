import { FC } from 'react';

import { BurgerSVG } from '@/components/svgs/headerSvgs/BurgerSVG';

export const Burger: FC = () => {
  return (
    <button className='focus:[&_svg_path]:fill-accent flex border-none bg-transparent'>
      <BurgerSVG />
    </button>
  );
};
