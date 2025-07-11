import { createContext } from 'react';

import { MainContextType } from './types/MainContextType';

export const MainContext = createContext<MainContextType>({
  isTempProfile: false,
  onTablet: false,
  onDesktop: false,
});
