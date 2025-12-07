import { Trainer } from '@/shared/types/trainer';

import { client } from './utils/fetchOnClient';
import { server } from './utils/fetchOnServer';

export const getTrainers = () => {
  return server.get<Trainer[]>(`/trainer`);
};

export const postTrainer = (data: unknown) => {
  return client.post<Trainer>(`/trainers`, data);
};

export const patchTrainer = (id: number, data: unknown) => {
  return client.patch<Trainer>(`/trainers/${id}`, data);
};

export const deleteTrainer = (id: number) => {
  return client.delete(`/trainers/${id}`);
};
