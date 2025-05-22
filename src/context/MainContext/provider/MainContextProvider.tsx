import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

import { getEvents } from '../../../api/events';
import { getTrainers } from '../../../api/trainers';
import { NavLinks } from '../../../enums/NavLinks';
import { EventInfoType } from '../../../types/EventInfoType';
import { TrainerInfoType } from '../../../types/TrainerInfoType';
import { MainContext } from '../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const { pathname } = useLocation();
  const [trainers, setTrainers] = useState<TrainerInfoType[]>([]);
  const [events, setEvents] = useState<EventInfoType[]>([]);

  // #region useEffects

  useEffect(() => {
    const getters = [getTrainers, getEvents];
    const setters = [setTrainers, setEvents];

    getters.forEach((get, index) => {
      type ElementsType = TrainerInfoType[] | EventInfoType[];

      const set = setters[index] as React.Dispatch<
        React.SetStateAction<ElementsType>
      >;

      get()
        .then(response => set(response))
        .catch(() => {
          throw new Error("We can't get data from the server");
        });
    });
  }, []);

  // #endregion

  const isTempProfile = pathname.startsWith(NavLinks.tempProfile);

  const providerValue = { isTempProfile, trainers, events };

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
