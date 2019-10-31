//------- Client libraries
import {Flow} from '@vaadin/flow-frontend/Flow';
import {Router} from '@vaadin/router';

//------- Application Views
import './theme';
import './src/main-layout';

//------- Configure flow
const {serverSideRoutes} = new Flow({
  imports: () => import('../target/frontend/generated-flow-imports')
});

//------- Configure Router
const routes = [
  {
    path: '/',
    component: 'main-layout',
    children: [
      {
        path: '/categories',
        title: 'Client Categories',
        component: 'client-categories',
        action: async () => {
          await import('./src/client-categories');
        }
      },
      // fallback to server-side Flow routes if no client-side routes match
      ...serverSideRoutes
    ]
  }
];

const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);


