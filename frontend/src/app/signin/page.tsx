'use client';

import { useEffect, useState } from 'react';

import { MainContent } from '@/modules/MainContent';
import { SignInSignUp } from '@/modules/MainContent/components/SignInSignUp';

export default function SignInPage() {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <MainContent>
      <SignInSignUp />
    </MainContent>
  );
}
