/* eslint-disable @typescript-eslint/no-explicit-any */
import { EventInfoType } from '../types/EventInfoType';

import { client } from './utils/fetchClient';

export const getEvents = () => {
  return client.get<EventInfoType[]>(`/events`);
};

export const postEvent = (data: any) => {
  return client.post<EventInfoType>(`/events`, data);
};

export const deleteEvent = (id: number) => {
  return client.delete(`/events/${id}`);
};

export const patchEvent = (id: number, data: any) => {
  return client.patch<EventInfoType>(`/events/${id}`, data);
};
