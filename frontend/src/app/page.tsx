import { getEvents } from '@/api/events';
import { getSports } from '@/api/sports';
import { getTrainers } from '@/api/trainers';
import { GET_DATA_ERROR } from '@/constants/errors';

import { AppClient } from './AppClient';

export default async function HomePage() {
  try {
    return (
      <AppClient
        sports={await getSports()}
        events={await getEvents()}
        trainers={await getTrainers()}
      />
    );
  } catch {
    throw new Error(GET_DATA_ERROR);
  }
}
