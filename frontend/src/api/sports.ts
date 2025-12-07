import { Sport } from '@/shared/types/sport';

import { server } from './utils/fetchOnServer';

export const getSports = () => server.get<Sport[]>(`/sport`);
