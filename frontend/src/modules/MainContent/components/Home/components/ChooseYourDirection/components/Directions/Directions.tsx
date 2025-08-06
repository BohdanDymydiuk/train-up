import React from 'react';

import { useAppSelector } from '../../../../../../../../store/store';

export const Directions: React.FC = () => {
  const sports = useAppSelector(state => state.sports);

  return (
    <>
      {sports.map(sport => {
        const { id, sportName } = sport;

        return <span key={id}>{sportName}</span>;
      })}
    </>
  );
};
