import { getEvents } from '@/api/events';
import { getSports } from '@/api/sports';
import { getTrainers } from '@/api/trainers';

import { AppClient } from './AppClient';

export default async function HomePage() {
  const [events, sports, trainers] = await Promise.all([
    getEvents(),
    getSports(),
    getTrainers(),
  ]);

  return <AppClient events={events} sports={sports} trainers={trainers} />;
}
