import { useDispatch, useSelector } from 'react-redux';

import { combineSlices, configureStore } from '@reduxjs/toolkit';

import { eventsSlice } from './features/events';
import { locationSlice } from './features/location';
import { trainersSlice } from './features/trainers';

const rootReducer = combineSlices(trainersSlice, eventsSlice, locationSlice);

export const store = configureStore({
  reducer: rootReducer,
});

type RootState = ReturnType<typeof rootReducer>;
type AppDispatch = typeof store.dispatch;

export const useAppSelector = useSelector.withTypes<RootState>();
export const useAppDispatch = useDispatch.withTypes<AppDispatch>();
