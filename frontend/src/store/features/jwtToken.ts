import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const jwtTokenSlice = createSlice({
  name: 'jwtToken',
  initialState: '',
  reducers: {
    setToken: (_, action: PayloadAction<string>) => action.payload,
  },
});

export const { actions } = jwtTokenSlice;
