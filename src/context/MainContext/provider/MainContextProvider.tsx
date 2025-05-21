import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

import { getTrainers } from '../../../api/trainers';
import { NavLinks } from '../../../enums/NavLinks';
import { TrainerInfoType } from '../../../types/TrainerInfoType';
import { MainContext } from '../MainContext';

interface Props {
  children: React.ReactNode;
}

export const MainContextProvider: React.FC<Props> = ({ children }) => {
  const { pathname } = useLocation();
  const [trainers, setTrainers] = useState<TrainerInfoType[]>([]);

  // #region useEffects

  useEffect(() => {
    getTrainers()
      .then(response => setTrainers(response))
      .catch(() => {
        throw new Error("We can't get data from the server");
      });
  }, []);

  // #endregion

  const isTempProfile = pathname.startsWith(NavLinks.tempProfile);

  const providerValue = { isTempProfile, trainers };

  return (
    <MainContext.Provider value={providerValue}>
      {children}
    </MainContext.Provider>
  );
};
