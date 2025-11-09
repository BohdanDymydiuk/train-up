import { Trainer } from '@/shared/types/trainer';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const trainersSlice = createSlice({
  name: 'trainers',
  initialState: [] as Trainer[],
  reducers: {
    setTrainers: (_, action: PayloadAction<Trainer[]>) => action.payload,
  },
});

export const { actions } = trainersSlice;
