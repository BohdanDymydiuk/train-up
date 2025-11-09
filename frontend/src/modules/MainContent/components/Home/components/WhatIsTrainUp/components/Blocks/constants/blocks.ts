import { ABOUT_STRINGS } from '@/constants/strings';

import { Block } from '../types/Block';

export const blocks: Block[] = [
  {
    imageSrc: '/images/avatars/avatar_1.png',
    title: ABOUT_STRINGS.clients.title,
    description: ABOUT_STRINGS.clients.description,
  },
  {
    imageSrc: '/images/avatars/avatar_2.png',
    title: ABOUT_STRINGS.trainers.title,
    description: ABOUT_STRINGS.trainers.description,
  },
  {
    imageSrc: '/images/avatars/avatar_3.png',
    title: ABOUT_STRINGS.gyms.title,
    description: ABOUT_STRINGS.gyms.description,
  },
];
