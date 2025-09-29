import { defineConfig } from 'vite';

export default defineConfig({
  root: '../platform-web/build/generated/teavm/js/',
  build: {
    outDir: './build/generated/capacitor',
    minify: false,
    emptyOutDir: false,
  },
});
