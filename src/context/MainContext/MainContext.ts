import { createContext } from 'react';

import { MainContextType } from './types/MainContextType';

export const MainContext = createContext<MainContextType>({
  onTablet: false,
  onDesktop: false,
});
