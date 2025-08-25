/* eslint-disable @typescript-eslint/no-explicit-any */
import { Trainer } from '../types/Trainer';

import { client } from './utils/fetchClient';

export const getTrainers = () => {
  return client.get<Trainer[]>(`/trainer`);
};

export const postTrainer = (data: any) => {
  return client.post<Trainer>(`/trainers`, data);
};

export const deleteTrainer = (id: number) => {
  return client.delete(`/trainers/${id}`);
};

export const patchTrainer = (id: number, data: any) => {
  return client.patch<Trainer>(`/trainers/${id}`, data);
};
