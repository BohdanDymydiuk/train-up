import { BASE_URL } from '@/constants/fetch';
import { RequestMethod } from '@/shared/types/fetch';
import { store } from '@/store';

// a promise resolved after a given delay
function wait(delay: number) {
  return new Promise(resolve => {
    setTimeout(resolve, delay);
  });
}

async function request<T>(
  url: string,
  method: RequestMethod = 'GET',
  data: unknown = null,
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
  post: <T>(url: string, data: unknown) => request<T>(url, 'POST', data),
  patch: <T>(url: string, data: unknown) => request<T>(url, 'PATCH', data),
  delete: (url: string) => request(url, 'DELETE'),
};
