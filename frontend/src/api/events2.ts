/* eslint-disable @typescript-eslint/no-explicit-any */
import { EventInfoType } from '@/shared/types/events';

import { client } from './utils/fetchClient';

export const getEvents = () => {
  return client.get<EventInfoType[]>(`/event`);
};

export const postEvent = (data: any) => {
  return client.post<EventInfoType>(`/event`, data);
};

export const deleteEvent = (id: number) => {
  return client.delete(`/event/${id}`);
};

export const patchEvent = (id: number, data: any) => {
  return client.patch<EventInfoType>(`/event/${id}`, data);
};
