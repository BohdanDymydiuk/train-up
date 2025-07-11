import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { TrainerInfoType } from '../../types/TrainerInfoType';

export const trainersSlice = createSlice({
  name: 'trainers',
  initialState: [] as TrainerInfoType[],
  reducers: {
    setTrainers: (_, action: PayloadAction<TrainerInfoType[]>) =>
      action.payload,
  },
});

export const { actions } = trainersSlice;
