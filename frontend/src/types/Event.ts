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
