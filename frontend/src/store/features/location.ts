import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const locationSlice = createSlice({
  name: 'location',
  initialState: '',
  reducers: {
    setLocation: (_, action: PayloadAction<string>) => action.payload,
  },
});

export const { actions } = locationSlice;
