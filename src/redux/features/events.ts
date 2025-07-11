import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { EventInfoType } from '../../types/EventInfoType';

export const eventsSlice = createSlice({
  name: 'events',
  initialState: [] as EventInfoType[],
  reducers: {
    setEvents: (_, action: PayloadAction<EventInfoType[]>) => action.payload,
  },
});

export const { actions } = eventsSlice;
