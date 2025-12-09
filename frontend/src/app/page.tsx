import { getEvents } from '@/api/events';
import { getSports } from '@/api/sports';
import { getTrainers } from '@/api/trainers';
import { GET_DATA_ERROR } from '@/constants/errors';

import { AppClient } from './AppClient';

export default async function HomePage() {
  const [events, sports, trainers] = await Promise.all([
    getEvents(),
    getSports(),
    getTrainers(),
  ]).catch(() => {
    throw new Error(GET_DATA_ERROR);
  });

  return <AppClient events={events} sports={sports} trainers={trainers} />;
}
