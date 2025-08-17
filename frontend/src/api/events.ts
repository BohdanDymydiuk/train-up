import { Event } from '../types/Event';

import { client } from './utils/fetchClient';

export const getEvents = () => {
  return client.get<Event[]>(`/event`);
};
