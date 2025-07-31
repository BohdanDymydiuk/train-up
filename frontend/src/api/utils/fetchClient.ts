/* eslint-disable @typescript-eslint/no-explicit-any */
import { store } from '../../store/store';

const BASE_URL = 'https://train-up.onrender.com/'; // for deploying: 'https://train-up.onrender.com/' for local: 'http://localhost:8080'

// a promise resolved after a given delay
function wait(delay: number) {
  return new Promise(resolve => {
    setTimeout(resolve, delay);
  });
}

// To have autocompletion and avoid mistypes
type RequestMethod = 'GET' | 'POST' | 'PATCH' | 'DELETE';

function request<T>(
  url: string,
  method: RequestMethod = 'GET',
  data: any = null, // we can send any data to the server
): Promise<T> {
  const options: RequestInit = { method };

  const state = store.getState();
  const jwtToken = state.jwtToken;

  options.headers = {
    'Content-Type': 'application/json; charset=UTF-8',
    Accept: 'application/json',
  };

  if (jwtToken) {
    // if the user has successfully signed in
    options.headers.Authorization = `Bearer ${jwtToken}`;
  }

  if (data) {
    // We add body and Content-Type only for the requests with data
    options.body = JSON.stringify(data);
  }

  // for a demo purpose we emulate a delay to see if Loaders work
  return wait(300)
    .then(() => {
      console.log(options);

      return fetch(BASE_URL + url, options);
    })
    .then(response => {
      console.log(response);

      return response.json();
    });
}

export const client = {
  get: <T>(url: string) => request<T>(url),
  post: <T>(url: string, data: any) => request<T>(url, 'POST', data),
  patch: <T>(url: string, data: any) => request<T>(url, 'PATCH', data),
  delete: (url: string) => request(url, 'DELETE'),
};
