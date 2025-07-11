/* eslint-disable @typescript-eslint/no-explicit-any */
const BASE_URL = 'http://localhost:8080';

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
  const jwtTokem =
    'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJsb3JlbTJAZ21haWwuY29tIiwiaWF0IjoxNzUxODA3NzU2LCJleHAiOjE3NTE4OTQxNTZ9.uQtsFNQA2lX5h5S4KIQlwBqBDYPBylDj6EAfWJ09FeRp-w-manthjE3r-3rv-mgh';

  options.headers = {
    'Content-Type': 'application/json; charset=UTF-8',
    Accept: 'application/json',
    Authorization: `Bearer ${jwtTokem}`,
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
