import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { Sport } from '../../types/Sport';

export const sportsSlice = createSlice({
  name: 'sports',
  initialState: [] as Sport[],
  reducers: {
    setSports: (_, action: PayloadAction<Sport[]>) => action.payload,
  },
});

export const { actions } = sportsSlice;
