import React, { useEffect, useState } from 'react';
import { useMediaQuery } from 'react-responsive';
import { useLocation } from 'react-router';

import { getEvents } from '../../../api/events';
import { getTrainers } from '../../../api/trainers';
import { NavLinks } from '../../../enums/NavLinks';
import { EventInfoType } from '../../../types/EventInfoType';
import { TrainerInfoType } from '../../../types/TrainerInfoType';
import { regionalCenters } from '../constants/regionalCenters';
import { MainContext } from '../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const { pathname } = useLocation();

  // #region states

  const [trainers, setTrainers] = useState<TrainerInfoType[]>([]);
  const [events, setEvents] = useState<EventInfoType[]>([]);
  const [location, setLocation] = useState('');

  // #endregion
  // #region media queries

  const onTablet = useMediaQuery({ query: '(min-width: 768px)' });

  // #endregion
  // #region useEffects

  useEffect(() => {
    const getters = [getTrainers, getEvents];
    const setters = [setTrainers, setEvents];

    getters.forEach((get, index) => {
      type ElementsType = TrainerInfoType[] | EventInfoType[];

      if (index === 1) {
        return;
      }

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

  useEffect(() => {
    function showLocation(position: GeolocationPosition) {
      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;

      const url = `https://us1.locationiq.com/v1/reverse?key=pk.7457ca726ad3e6d451be7db68b10d1c2&lat=${latitude}&lon=${longitude}&format=json&accept-language=ua`;

      fetch(url)
        .then(response => response.json())
        .then(data => {
          const state: string = data.address.state;
          const city: string = regionalCenters[state];

          setLocation(city);
        })
        .catch(() => {
          throw new Error("Sorry, we can't find you. Check your permissions");
        });
    }

    navigator.geolocation.getCurrentPosition(showLocation);
  }, []);

  useEffect(() => {
    const currentDay = new Date().getDay();

    console.log(currentDay);
  }, []);

  console.log(trainers);

  // #endregion

  const isTempProfile = pathname.startsWith(NavLinks.tempProfile);

  const providerValue = { isTempProfile, trainers, events, location, onTablet };

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
