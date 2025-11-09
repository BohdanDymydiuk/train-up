import type React from 'react';

export interface Event {
  id: number;
  name: string;
  sportId: number | null;
  description: string;
  dateTime: string;
  gymId: number | null;
  trainerId: number;
  onlineTraining: boolean;
  intensity: 1 | 2 | 3;
  photoUrls: string[];
}

export interface EventInfoType {
  id: number;
  name: string;
  description: string;
  intensity: 0 | 1 | 2;
  participants: number;
  trainingTypes: string[];
  trainer: string;
}

export type InputChangeEvent = React.ChangeEvent<HTMLInputElement>;
