import { Sport } from '@/types/Sport';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const sportsSlice = createSlice({
  name: 'sports',
  initialState: [] as Sport[],
  reducers: {
    setSports: (_, action: PayloadAction<Sport[]>) => action.payload,
  },
});

export const { actions } = sportsSlice;
