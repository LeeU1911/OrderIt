import { Response } from 'express';

interface Client {
  id: number;
  res: Response;
}

let clients: Client[] = [];
let clientId = 0;

export function addClient(res: Response) {
  const headers = {
    'Content-Type': 'text/event-stream',
    'Connection': 'keep-alive',
    'Cache-Control': 'no-cache'
  };
  res.writeHead(200, headers);

  const newClient: Client = {
    id: ++clientId,
    res
  };
  clients.push(newClient);

  res.write(`data: ${JSON.stringify({ type: 'connected' })}\
\
`);

  res.on('close', () => {
    clients = clients.filter(client => client.id !== newClient.id);
  });
}

export function broadcast(event: string, payload: any) {
  const data = `data: ${JSON.stringify({ type: event, payload })}\
\
`;
  clients.forEach(client => client.res.write(data));
}
