/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'export', // Outputs a Single-Page Application (SPA).
  distDir: './dist', // Changes the build output directory to `./dist/`.
  sassOptions: {
    additionalData: `
      @use "/src/fonts.scss" as *;
      @use "/src/utils/vars.scss" as *;
      @use "/src/utils/mixins.scss" as *;
    `,
  },
};

export default nextConfig;
