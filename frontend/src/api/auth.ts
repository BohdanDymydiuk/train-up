import { Token } from '@/shared/types/auth';

import { client } from './utils/fetchOnClient';

export const login = (data: unknown) => client.post<Token>('/auth/login', data);
