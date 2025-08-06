import { Sport } from '../types/Sport';

import { client } from './utils/fetchClient';

export const getSports = () => {
  return client.get<Sport[]>(`/sport`);
};
