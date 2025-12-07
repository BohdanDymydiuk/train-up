import { EventInfoType as Event } from '@/shared/types/events';

import { client } from './utils/fetchOnClient';

export const getEvents = () => {
  return client.get<Event[]>(`/event`);
};

export const postEvent = (data: unknown) => {
  return client.post<Event>(`/event`, data);
};

export const patchEvent = (id: number, data: unknown) => {
  return client.patch<Event>(`/event/${id}`, data);
};

export const deleteEvent = (id: number) => {
  return client.delete(`/event/${id}`);
};
