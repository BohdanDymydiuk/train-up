import React, { useEffect, useState } from 'react';

import { Events } from './components/Events';
import { Trainers } from './components/Trainers';
import { regionalCenters } from './constants/regionalCenters';

import styles from './ProfileMain.module.scss';

export const ProfileMain: React.FC = () => {
  const [city, setCity] = useState('');

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

          setCity(city);
        })
        .catch(error => console.log(error));
    }

    navigator.geolocation.getCurrentPosition(showLocation);
  }, []);

  return (
    <div className={styles['profile-main']}>
      <div
        style={{
          position: 'absolute',
          color: 'white',
          backgroundColor: 'black',
        }}
      >
        You're located in {city}
      </div>
      <Trainers />
      <Events />
    </div>
  );
};
