//------- Client libraries
import {Flow} from '@vaadin/flow-frontend/Flow';
import {Router} from '@vaadin/router';

//------- Application Views
import './theme';
import './src/main-layout';
import './src/client-categories';

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
        path: '/categories',
        title: 'Client Categories',
        component: 'client-categories',
        action: async () => {
          // FIXME: remove the static import above and import the component dynamically
          // when https://github.com/vaadin/flow/issues/6295 is fixed
          // await import('./src/client-categories');
        }
      },
      {
        // fallback to server-side Flow routes if no client-side routes match
        path: '(.*)',
        action: async (context: any): Promise<HTMLElement> => {
          // FIXME: create the Flow fallback route with flow.route()
          // when https://github.com/vaadin/flow/issues/6338 is implemented
          const view = await flow.navigate(context);
          const elem = document.createElement('div');
          elem.appendChild(view);
          return elem;
        }
      }
    ]
  }
];

const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);


