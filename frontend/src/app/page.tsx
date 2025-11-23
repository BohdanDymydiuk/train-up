'use client';

import { MainContent } from '@/modules/MainContent';
import { Home } from '@/modules/MainContent/components/Home';
import { ProfileMain } from '@/modules/MainContent/components/ProfileMain';
import { useAppSelector } from '@/store';

export default function HomePage() {
  const jwtToken = useAppSelector(state => state.jwtToken);

  return <MainContent>{jwtToken ? <ProfileMain /> : <Home />}</MainContent>;
}
