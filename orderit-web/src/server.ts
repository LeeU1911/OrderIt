import { createApp } from './app.js';

const PORT = Number(process.env.PORT || 3001);
const HOST = process.env.HOST || '0.0.0.0';
const DB_PATH = process.env.DB_PATH || './data/orderit.db';

const { app, close } = createApp(DB_PATH);

const server = app.listen(PORT, HOST, () => {
  console.log(`Server is running on http://${HOST}:${PORT}`);
});

function shutdown() {
  console.log('Shutting down...');
  server.closeAllConnections?.();
  server.close(() => {
    close();
    process.exit(0);
  });
}

process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
