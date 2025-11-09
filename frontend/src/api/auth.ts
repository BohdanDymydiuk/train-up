/* eslint-disable @typescript-eslint/no-explicit-any */
import { Token } from '@/shared/types/auth';

import { client } from './utils/fetchClient';

export const login = (data: any) => {
  return client.post<Token>('/auth/login', data);
};
