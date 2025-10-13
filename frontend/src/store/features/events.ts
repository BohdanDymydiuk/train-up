import { Event } from '@/types/Event';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const eventsSlice = createSlice({
  name: 'events',
  initialState: [] as Event[],
  reducers: {
    setEvents: (_, action: PayloadAction<Event[]>) => action.payload,
  },
});

export const { actions } = eventsSlice;
