import React, { useEffect, useState } from 'react';

import { Events } from './components/Events';
import { Trainers } from './components/Trainers';

import styles from './ProfileMain.module.scss';

export const ProfileMain: React.FC = () => {
  const [location, setLocation] = useState('');

  useEffect(() => {
    function showCity(position: GeolocationPosition) {
      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;

      const url = `https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`;

      fetch(url)
        .then(response => response.json())
        .then(data => {
          const city = data.address.city;
          const village: string = data.address.village;

          setLocation(city || village);
        })
        .catch(error => console.log(error));
    }

    navigator.geolocation.getCurrentPosition(showCity);
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
        You're located in {location}
      </div>
      <Trainers />
      <Events />
    </div>
  );
};
