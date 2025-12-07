import { BASE_URL } from '@/constants/fetch';

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(BASE_URL + url, {
    ...options,
    cache: 'no-store',
  });

  // if (!res.ok) {
  //   throw new Error(`Request failed with status ${res.status}`);
  // }

  return response.json();
}

export const server = {
  get: <T>(url: string) => request<T>(url),
};
