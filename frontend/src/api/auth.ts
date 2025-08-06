/* eslint-disable @typescript-eslint/no-explicit-any */
import { Token } from '../types/Token';

import { client } from './utils/fetchClient';

export const login = (data: any) => {
  return client.post<Token>('/auth/login', data);
};
