'use client';

import React from 'react';

import { MainContent } from '@/modules/MainContent';
import { SignInSignUp } from '@/modules/MainContent/components/SignInSignUp';

export default function SignInPage() {
  const [isMounted, setIsMounted] = React.useState(false);

  React.useEffect(() => {
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
