import { createContext } from 'react';

import { MainContextType } from '@/shared/types/context';

export const MainContext = createContext<MainContextType>({
  onTablet: false,
  onSmallDesktop: false,
  onDesktop: false,
  eventWidth: '',
});
