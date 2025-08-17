import React, { useEffect } from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router';

import { getEvents } from './api/events';
import { getEvents as getEvents2 } from './api/events2';
import { getSports } from './api/sports';
import { getTrainers } from './api/trainers';
import { GET_DATA_ERROR } from './constants/errors';
import { REGIONAL_CENTERS } from './constants/regionalCenters';
import { MainContextProvider } from './context/MainContext/provider/MainContextProvider';
import { Links } from './enums/Links';
import { MainContent } from './modules/MainContent';
import { Home } from './modules/MainContent/components/Home';
import { ProfileMain } from './modules/MainContent/components/ProfileMain';
import { SignIn } from './modules/MainContent/components/SignIn';
import { actions as eventsActions } from './store/features/events';
import { actions as eventsActions2 } from './store/features/events2';
import { actions as locationActions } from './store/features/location';
import { actions as sportsActions } from './store/features/sports';
import { actions as trainersActions } from './store/features/trainers';
import { useAppDispatch, useAppSelector } from './store/store';
import { Event } from './types/Event';
import { EventInfoType } from './types/EventInfoType';
import { Sport } from './types/Sport';
import { TrainerInfoType } from './types/TrainerInfoType';

import './App.scss';

export const App: React.FC = () => {
  const dispatch = useAppDispatch();
  const jwtToken = useAppSelector(state => state.jwtToken);

  // #region setValue
  const setTrainers = (trainers: TrainerInfoType[]) => {
    dispatch(trainersActions.setTrainers(trainers));
  };

  const setEvents = (events: Event[]) => {
    dispatch(eventsActions.setEvents(events));
  };

  const setEvents2 = (events: EventInfoType[]) => {
    dispatch(eventsActions2.setEvents(events));
  };

  const setLocation = (location: string) => {
    dispatch(locationActions.setLocation(location));
  };

  const setSports = (sports: Sport[]) => {
    dispatch(sportsActions.setSports(sports));
  };
  // #endregion

  // #region useEffects
  useEffect(() => {
    const getters = [getSports, getEvents];
    const setters = [setSports, setEvents];

    getters.forEach((get, index) => {
      type ElementsType = Sport[] | Event[];

      const set = setters[index] as (value: ElementsType) => void;

      get()
        .then(response => set(response))
        .catch(() => {
          throw new Error(GET_DATA_ERROR);
        });
    });
  }, []);

  useEffect(() => {
    if (jwtToken) {
      const getters = [getTrainers, getEvents2];
      const setters = [setTrainers, setEvents2];

      getters.forEach((get, index) => {
        type ElementsType = TrainerInfoType[] | EventInfoType[];

        if (index === 1) {
          return;
        }

        const set = setters[index] as (value: ElementsType) => void;

        get()
          .then(response => set(response))
          .catch(() => {
            throw new Error(GET_DATA_ERROR);
          });
      });
    }
  }, [jwtToken]);

  useEffect(() => {
    function showLocation(position: GeolocationPosition) {
      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;

      const url = `https://us1.locationiq.com/v1/reverse?key=pk.7457ca726ad3e6d451be7db68b10d1c2&lat=${latitude}&lon=${longitude}&format=json&accept-language=ua`;

      fetch(url)
        .then(response => response.json())
        .then(data => {
          const state: string = data.address.state;
          const city: string = REGIONAL_CENTERS[state];

          setLocation(city);
        })
        .catch(() => {
          throw new Error("Sorry, we can't find you. Check your permissions");
        });
    }

    navigator.geolocation.getCurrentPosition(showLocation);
  }, []);
  // #endregion

  const titleCssProps: React.CSSProperties = {
    position: 'absolute',
    visibility: 'hidden',
  };

  return (
    <>
      <h1 style={titleCssProps}>TrainUp</h1>
      <Router>
        <MainContextProvider>
          <Routes>
            <Route path='/' element={<MainContent />}>
              <Route index element={jwtToken ? <ProfileMain /> : <Home />} />
              <Route path={Links.signIn} element={<SignIn />} />
            </Route>
          </Routes>
        </MainContextProvider>
      </Router>
    </>
  );
};
