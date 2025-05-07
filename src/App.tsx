import React from 'react';

import { Route, BrowserRouter as Router, Routes } from 'react-router';

import { LogIn } from './modules/LogIn';
import { Main } from './modules/Main';

import './App.scss';

export const App: React.FC = () => {
  const titleStyles: React.CSSProperties = {
    position: 'absolute',
    visibility: 'hidden',
  };

  return (
    <>
      <h1 style={titleStyles}>TrainUp</h1>
      <Router>
        <Routes>
          <Route path='/' element={<Main />} />
          <Route path='/login' element={<LogIn />} />
        </Routes>
      </Router>
    </>
  );
};
