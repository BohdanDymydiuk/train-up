import { Sport } from '@/shared/types/sport';

import { client } from './utils/fetchClient';

export const getSports = () => {
  return client.get<Sport[]>(`/sport`);
};
