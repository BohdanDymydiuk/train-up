import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { Event } from '../../types/Event';

export const eventsSlice = createSlice({
  name: 'events',
  initialState: [] as Event[],
  reducers: {
    setEvents: (_, action: PayloadAction<Event[]>) => action.payload,
  },
});

export const { actions } = eventsSlice;
