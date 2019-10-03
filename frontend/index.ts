//------- Client libraries
import {Flow} from '@vaadin/flow-frontend/Flow';
import {Router} from '@vaadin/router';
import client from './generated/connect-client.default';

//------- Application Views
import './theme';
import './src/main-layout';

//------- Configure flow
const flow = new Flow({
  //@ts-ignore
  imports: () => import('../target/frontend/generated-flow-imports')
});

//------- Configure Router
const routes = [
  {
    path: '/',
    component: 'main-layout',
    children: [
      {
        path: '/client-reviews',
        component: 'client-reviews',
        // @ts-ignore
        action: () => import('./src/client-reviews')
      },
      {
        path: '/client-categories',
        component: 'client-categories',
        action: () => import('./src/client-categories')
      },
      // fallback to server-side Flow routes if no client-side routes match
      ...flow.serverSideRoutes
    ]
  }
];

const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);

/// FIXME: Disable interactive login right now
client.credentials = function() {
  return new Promise((resolve) => {
      resolve({
          username: 'user',
          password: 'user',
          stayLoggedIn: true
      })
  })
}


