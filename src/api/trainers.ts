/* eslint-disable @typescript-eslint/no-explicit-any */
import { TrainerInfoType } from '../types/TrainerInfoType';

import { client } from './utils/fetchClient';

export const getTrainers = () => {
  return client.get<TrainerInfoType[]>(`/trainers`);
};

export const postTrainer = (data: any) => {
  return client.post<TrainerInfoType>(`/trainers`, data);
};

export const deleteTrainer = (id: number) => {
  return client.delete(`/trainers/${id}`);
};

export const patchTrainer = (id: number, data: any) => {
  return client.patch<TrainerInfoType>(`/trainers/${id}`, data);
};
