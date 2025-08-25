import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { Trainer } from '../../types/Trainer';

export const trainersSlice = createSlice({
  name: 'trainers',
  initialState: [] as Trainer[],
  reducers: {
    setTrainers: (_, action: PayloadAction<Trainer[]>) => action.payload,
  },
});

export const { actions } = trainersSlice;
