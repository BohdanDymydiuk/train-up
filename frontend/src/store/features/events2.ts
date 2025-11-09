import { EventInfoType } from '@/shared/types/events';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const eventsSlice2 = createSlice({
  name: 'events2',
  initialState: [] as EventInfoType[],
  reducers: {
    setEvents: (_, action: PayloadAction<EventInfoType[]>) => action.payload,
  },
});

export const { actions } = eventsSlice2;
