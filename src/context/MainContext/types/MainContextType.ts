import { EventInfoType } from '../../../types/EventInfoType';
import { TrainerInfoType } from '../../../types/TrainerInfoType';

export interface MainContextType {
  isTempProfile: boolean;
  trainers: TrainerInfoType[];
  events: EventInfoType[];
  location: string;
  onTablet: boolean;
}
