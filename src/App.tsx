import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router';

import { MainContextProvider } from './context/MainContext/provider/MainContextProvider';
import { NavLinks } from './enums/NavLinks';
import { MainContent } from './modules/MainContent';
import { Home } from './modules/MainContent/components/Home';
import { ProfileMain } from './modules/MainContent/components/ProfileMain';
import { SignIn } from './modules/MainContent/components/SignIn';

import './App.scss';

export const App: React.FC = () => {
  const titleCssProps: React.CSSProperties = {
    position: 'absolute',
    visibility: 'hidden',
  };

  return (
    <>
      <h1 style={titleCssProps}>TrainUp</h1>
      <Router>
        <MainContextProvider>
          <Routes>
            <Route path='/' element={<MainContent />}>
              <Route index element={<Home />} />
              <Route path={NavLinks.signIn} element={<SignIn />} />
              <Route path={NavLinks.tempProfile} element={<ProfileMain />} />
            </Route>
          </Routes>
        </MainContextProvider>
      </Router>
    </>
  );
};
