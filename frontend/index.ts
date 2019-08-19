
import '@vaadin/vaadin-lumo-styles/icons.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/color.js';

import './src/main-layout';
// FIXME: does not work in action
import './src/client-categories';

import { Flow } from '@vaadin/flow-frontend/Flow';
//@ts-ignore
import { Router } from '@vaadin/router';
const flow = new Flow({
    //@ts-ignore
    imports: () => import('../target/frontend/generated-flow-imports')
});

const routes = [
    {
        path: '/',
        component: 'main-layout',
        children: [
            {
                path: '/client-categories',
                title: 'Client Categories',
                component: 'client-categories',
                action: async () => {
                    // FIXME.
                    //@ts-ignore
                    // await import('./src/client-categories');
                }
            },
            {
                // fallback to Flow if no client-side routes match
                path: '(.*)',
                action: async (cfg: any) => {

                    const view = await flow.navigate(cfg);
                    view.className = 'view-container';

                    // FIXME: workaround for https://github.com/vaadin/vaadin-router/issues/357
                    const $wnd = (window as any);
                    $wnd.cont = $wnd.cont || 0;
                    const main = document.createElement('div');
                    main.id = `main-${$wnd.cont++}`;
                    main.className = 'view-container';

                    main.appendChild(view);
                    return main;

                }
            }
        ]
    }
];

const router = new Router(document.querySelector('#outlet'), {});
router.setRoutes(routes);


