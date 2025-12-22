import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://backend:8080', // Docker service name
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '')
                // Note: Your backend routes are mixed (some start with /api, some don't).
                // For local dev, use http://localhost:8080.
                // In Docker, we will handle this via Nginx or direct proxy.
            }
        }
    }
})