export interface EventInfoType {
  id: number;
  name: string;
  description: string;
  intensity: 0 | 1 | 2;
  participants: number;
  trainingTypes: string[];
  trainer: string;
}
