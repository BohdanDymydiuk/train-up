'use client';

import { useEffect } from 'react';

import { getEvents } from '@/api/events';
import { getEvents as getEvents2 } from '@/api/events2';
import { getSports } from '@/api/sports';
import { getTrainers } from '@/api/trainers';
import { GET_DATA_ERROR } from '@/constants/errors';
import { REGIONAL_CENTERS } from '@/constants/regionalCenters';
import { ERROR_STRINGS } from '@/constants/strings';
import { Event, EventInfoType } from '@/shared/types/events';
import { Sport } from '@/shared/types/sport';
import { Trainer } from '@/shared/types/trainer';
import { useAppDispatch, useAppSelector } from '@/store';
import { actions as eventsActions } from '@/store/features/events';
import { actions as eventsActions2 } from '@/store/features/events2';
import { actions as locationActions } from '@/store/features/location';
import { actions as sportsActions } from '@/store/features/sports';
import { actions as trainersActions } from '@/store/features/trainers';

export function AppClient() {
  const dispatch = useAppDispatch();
  const jwtToken = useAppSelector(state => state.jwtToken);

  // #region setValue
  const setTrainers = (trainers: Trainer[]) => {
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
    const getDataFns = [getSports, getEvents, getTrainers];
    const setDataFns = [setSports, setEvents, setTrainers];

    setDataHandler(getDataFns, setDataFns);
  }, []);

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

  return null;
}
