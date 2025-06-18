import React from 'react';

interface Props {
  [key: string]: unknown;
}

export const DpHoc = (Component: React.FC) => {
  return (props: Props) => {
    return <Component {...props} />;
  };
};
