import { Event } from '@/shared/types/events';

import { client } from './utils/fetchClient';

export const getEvents = () => {
  return client.get<Event[]>(`/event`);
};
