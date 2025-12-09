'use client';

import { useEffect } from 'react';

import { getEvents as getEvents2 } from '@/api/events2';
import { GET_DATA_ERROR } from '@/constants/errors';
import { REGIONAL_CENTERS } from '@/constants/regionalCenters';
import { ERROR_STRINGS } from '@/constants/strings';
import { MainContent } from '@/modules/MainContent';
import { Home } from '@/modules/MainContent/components/Home';
import { ProfileMain } from '@/modules/MainContent/components/ProfileMain';
import { Event, EventInfoType } from '@/shared/types/events';
import { Sport } from '@/shared/types/sport';
import { Trainer } from '@/shared/types/trainer';
import { useAppDispatch, useAppSelector } from '@/store';
import { actions as eventsActions } from '@/store/features/events';
import { actions as eventsActions2 } from '@/store/features/events2';
import { actions as locationActions } from '@/store/features/location';
import { actions as sportsActions } from '@/store/features/sports';
import { actions as trainersActions } from '@/store/features/trainers';

interface Props {
  events: Event[];
  sports: Sport[];
  trainers: Trainer[];
}

export const AppClient: React.FC<Props> = ({ events, sports, trainers }) => {
  const dispatch = useAppDispatch();
  const jwtToken = useAppSelector(state => state.jwtToken);

  // #region setValue
  const setSports = (sports: Sport[]) => {
    dispatch(sportsActions.setSports(sports));
  };

  const setEvents = (events: Event[]) => {
    dispatch(eventsActions.setEvents(events));
  };

  const setTrainers = (trainers: Trainer[]) => {
    dispatch(trainersActions.setTrainers(trainers));
  };

  const setEvents2 = (events: EventInfoType[]) => {
    dispatch(eventsActions2.setEvents(events));
  };

  const setLocation = (location: string) => {
    dispatch(locationActions.setLocation(location));
  };
  // #endregion

  // #region useEffects
  const setDataHandler = (
    getDataFns: (() => Promise<unknown[]>)[],
    setDataFns: unknown[],
  ) => {
    getDataFns.forEach((getData, index) => {
      const setData = setDataFns[index] as (value: unknown[]) => void;

      getData()
        .then(response => setData(response))
        .catch(() => {
          throw new Error(GET_DATA_ERROR);
        });
    });
  };

  useEffect(() => {
    setSports(sports);
    setEvents(events);
    setTrainers(trainers);
  }, [sports, events, trainers]);

  useEffect(() => {
    if (jwtToken) {
      const getDataFns = [getEvents2];
      const setDataFns = [setEvents2];

      setDataHandler(getDataFns, setDataFns);
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
          throw new Error(ERROR_STRINGS.locationNotFound);
        });
    }

    navigator.geolocation.getCurrentPosition(showLocation);
  }, []);
  // #endregion

  return <MainContent>{jwtToken ? <ProfileMain /> : <Home />}</MainContent>;
};
