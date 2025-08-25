export interface Trainer {
  id: number;
  name: string;
  categories: string[];
  bio: string;
  reviews: number;
  isNew: boolean;
  trainingTypes: string[];
  sportIds: number[];
}
