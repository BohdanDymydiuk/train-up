import { Event } from '@/shared/types/events';

import { server } from './utils/fetchOnServer';

export const getEvents = () => server.get<Event[]>(`/event`);
