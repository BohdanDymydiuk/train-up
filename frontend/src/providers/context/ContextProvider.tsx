import {
  createContext,
  FC,
  ReactNode,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useMediaQuery } from 'react-responsive';

import { NavItems } from '@/shared/enums';
import { MainContextType } from '@/shared/types/context';

const Context = createContext<MainContextType>({
  onTablet: false,
  onSmallDesktop: false,
  onDesktop: false,
  eventWidth: '',
});

interface Props {
  children: ReactNode;
}

const ContextProvider: FC<Props> = ({ children }) => {
  const [currentSection, setCurrentSection] = useState(NavItems.main);

  const onTablet = useMediaQuery({ query: '(min-width: 768px)' });
  const onSmallDesktop = useMediaQuery({ query: '(min-width: 1024px)' });
  const onDesktop = useMediaQuery({ query: '(min-width: 1200px)' });

  useEffect(() => {
    const currentDay = new Date().getDay();

    console.log(currentDay);
  }, []);

  const eventWidth = useMemo(() => {
    if (onTablet) {
      return '422px';
    }

    return '100%';
  }, [onTablet]);

  const providerValue = useMemo(
    () => ({
      onTablet,
      onSmallDesktop,
      onDesktop,
      currentSection,
      eventWidth,
      setCurrentSection,
    }),
    [onTablet, onSmallDesktop, onDesktop, currentSection],
  );

  return <Context.Provider value={providerValue}>{children}</Context.Provider>;
};

export { ContextProvider, Context };
