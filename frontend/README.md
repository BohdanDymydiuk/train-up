# TrainUp Frontend

Frontend for TrainUp built with:

- `Next.js 15.5.4`
- `React 19.0.0`
- `Tailwind CSS 4.1.18`
- `TypeScript 5.7.2`

## Commands

```bash
npm install
npm run dev
npm run build
npm run start
npm run lint
npm run format
```

## Styling

- Tailwind CSS is the main styling approach.
- `clsx(...)` is used for local class composition.
- Global design tokens live in `frontend/src/globals.css` inside `@theme`.

## Prettier

In `frontend/.prettierrc`:

```json
"tailwindFunctions": ["clsx"]
```

This allows Prettier to sort Tailwind classes inside `clsx(...)`.

## Tailwind Migration

The Tailwind migration is complete.

One intentional exception remains:
- `frontend/src/modules/MainContent/components/Home/components/SportFinder/SportFinder.module.css`

Part of `SportFinder` still uses CSS on purpose because that code is clearer and easier to maintain there than in utility classes.

## Fonts

- `Inter` is applied globally through `next/font/google`.
- `Ermilov` and `WF Visual Sans` are loaded through `next/font/local`.
- I do not add `sans-serif` fallback by default, because it looks fine without it in the current setup.

## Notes

- `SportFinder.module.css` uses `:export` to share color tokens with `Button.tsx`. This works correctly with CSS Modules, but may still produce a non-blocking CSS optimization warning during build.
- Revisit the commented-out `CSSProperties` block in `frontend/src/modules/MainContent/components/Header/Header.tsx` and remove it later if it is no longer needed.
