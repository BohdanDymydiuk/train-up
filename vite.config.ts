import react from '@vitejs/plugin-react';

import { defineConfig } from 'vite';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/',
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
          @use "/src/fonts.scss" as *;
          @use "/src/utils/vars.scss" as *;
          @use "/src/utils/mixins.scss" as *;
        `,
      },
    },
  },
});
