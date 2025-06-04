/* eslint-disable @typescript-eslint/no-explicit-any */
const BASE_URL = 'https://a984-109-200-252-189.ngrok-free.app';

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
  options.headers = {
    'Content-Type': 'application/json; charset=UTF-8',
    Authorization:
      'Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJsb3JlbTJAZ21haWwuY29tIiwiaWF0IjoxNzQ5MDQwMzUwLCJleHAiOjE3NDkwNDMzNTB9.xE21AJ3N_UAiDk7SKogSA92sCoEvD0yx-pwVabQS0Lg24jv1KVwHWPfSdad-08eO',
  };

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
