// Common strings
export const COMMON_STRINGS = {
  appName: 'TrainUp',
  learnMore: 'Дізнатись більше',
} as const;

// Authentication/Registration form
export const AUTH_STRINGS = {
  welcome: 'Вітаємо у TrainUp',
  emailPlaceholder: 'Введіть e-mail',
  passwordPlaceholder: 'Пароль',
  signIn: 'Увійти',
  noAccount: 'Досі немає акаунта?',
  signUp: 'Зареєструватись',
  signUpTitle: 'Зареєструйтеся',
  signInAs: 'Увійти як',
  signInAsClient: 'Увійти як клієнт',
  signInAsTrainer: 'Увійти як тренер',
  signInAsGymAdmin: 'Увійти як адміністратор залу',
} as const;

// Sport finder
export const SPORT_FINDER_STRINGS = {
  title: {
    firstPart: 'Твій спорт — твій вибір.',
    secondPart: 'Ми поруч на кожному етапі шляху',
  },
  online: 'Онлайн',
  search: 'Шукати',
  searchPlaceholder: 'Пошук',
} as const;

// About TrainUp
export const ABOUT_STRINGS = {
  title: 'TrainUp — це',
  clients: {
    title: 'Клієнти',
    description:
      'Шукай тренерів і спортзали у своєму місті або онлайн. Порівнюй, читай відгуки та обирай найкраще — твій спорт починається тут.',
  },
  trainers: {
    title: 'Тренери',
    description:
      'Створюй профіль, додавай послуги та керуй записами — допоможемо тобі зосередитись на тренуваннях, а не на пошуку клієнтів.',
  },
  gyms: {
    title: 'Спортзали',
    description:
      'Додавай свій зал на платформу, автоматизуй бронювання і знаходь нових відвідувачів — ми допомагаємо залам зростати онлайн.',
  },
} as const;

// Trainer registration
export const TRAINER_SIGNUP_STRINGS = {
  title: {
    firstPart: 'Зареєструйся тренером та',
    secondPart: 'заробляй разом з',
  },
  appName: 'TrainUp',
  button: 'Стати тренером',
} as const;

// Direction selection
export const DIRECTION_STRINGS = {
  title: 'Оберіть свій напрям',
  showAll: 'Переглянути всі',
  hide: 'Приховати',
} as const;

// Events
export const EVENTS_STRINGS = {
  upcoming: 'Найближчі події',
} as const;

// Footer
export const FOOTER_STRINGS = {
  title: 'Твоя форма. Твій темп. Твій простір.',
  pages: 'Сторінки',
  socials: 'Ми в соцмережах',
} as const;

// Trainer
export const TRAINER_STRINGS = {
  newTrainer: 'Новий тренер',
} as const;

// Alt texts for images
export const ALT_STRINGS = {
  image: 'Image',
  avatar: 'avatar',
  celebration: 'Celebration',
  cross: 'cross',
  filledThunder: 'filled-thunder',
  thunder: 'thunder',
} as const;

// Errors and messages
export const ERROR_STRINGS = {
  locationNotFound: "Sorry, we can't find you. Check your permissions",
} as const;
