import clsx from 'clsx';

export const TW_TYPO_INTER_400 = clsx('font-normal');
export const TW_TYPO_INTER_400_14 = clsx(TW_TYPO_INTER_400, 'text-sm');
export const TW_TYPO_INTER_400_16 = clsx(TW_TYPO_INTER_400, 'text-base');

export const TW_TYPO_INTER_500 = clsx('font-medium');
export const TW_TYPO_INTER_500_12 = clsx(TW_TYPO_INTER_500, 'text-xs');
export const TW_TYPO_INTER_500_14 = clsx(TW_TYPO_INTER_500, 'text-sm');
export const TW_TYPO_INTER_500_15 = clsx(TW_TYPO_INTER_500, 'text-15');
export const TW_TYPO_INTER_500_16 = clsx(TW_TYPO_INTER_500, 'text-base');
export const TW_TYPO_INTER_500_18 = clsx(TW_TYPO_INTER_500, 'text-lg');
export const TW_TYPO_INTER_500_20 = clsx(TW_TYPO_INTER_500, 'text-xl');
export const TW_TYPO_INTER_500_24 = clsx(TW_TYPO_INTER_500, 'text-2xl');
export const TW_TYPO_INTER_500_40 = clsx(TW_TYPO_INTER_500, 'text-4xl');
export const TW_TYPO_INTER_500_48 = clsx(TW_TYPO_INTER_500, 'text-5xl');

export const TW_TYPO_INTER_600 = clsx('font-semibold');
export const TW_TYPO_INTER_600_12 = clsx(TW_TYPO_INTER_600, 'text-xs');
export const TW_TYPO_INTER_600_20 = clsx(TW_TYPO_INTER_600, 'text-xl');
export const TW_TYPO_INTER_600_24 = clsx(TW_TYPO_INTER_600, 'text-2xl');
export const TW_TYPO_INTER_600_30 = clsx(TW_TYPO_INTER_600, 'text-3xl');
export const TW_TYPO_INTER_600_40 = clsx(TW_TYPO_INTER_600, 'text-4xl');

export const TW_FONT_ERMILOV = clsx('font-[Ermilov]');
export const TW_TYPO_ERMILOV_24 = clsx(TW_FONT_ERMILOV, 'text-2xl');
export const TW_TYPO_ERMILOV_32 = clsx(TW_FONT_ERMILOV, 'text-32');
export const TW_TYPO_ERMILOV_40 = clsx(TW_FONT_ERMILOV, 'text-4xl');
export const TW_TYPO_ERMILOV_56 = clsx(TW_FONT_ERMILOV, 'text-6xl');

export const TW_FONT_WF_VISUAL = clsx('font-[WF_Visual_Sans]');
export const TW_TYPO_WF_VISUAL_500 = clsx(TW_FONT_WF_VISUAL, 'font-medium');

export const TW_BUTTON_BASE = clsx(
  TW_TYPO_INTER_500_15,
  'bg-accent focus:bg-danger cursor-pointer rounded-lg border-none text-white transition-[background-color] duration-200',
);

export const TW_INPUT_BASE = clsx('h-12 rounded-lg px-4 py-0');

export const TW_DROPDOWN_BASE = clsx(
  'border-ui-gray-200 absolute z-1000 origin-top scale-y-0 overflow-hidden rounded-2xl border opacity-0 transition-[opacity,transform] duration-200',
);

export const TW_DROPDOWN_SHOWN = clsx('scale-y-100 opacity-100');

export const TW_DROPDOWN_LIST = clsx(
  'list-none bg-white',
  TW_TYPO_INTER_500_14,
);

export const TW_DROPDOWN_ITEM = clsx(
  'not-last:border-ui-gray-200 hover:bg-surface-soft flex h-12 cursor-pointer items-center px-3.75 not-last:border-b',
);

export const TW_DROPDOWN_ITEM_LINK = clsx('text-dark text-right');
