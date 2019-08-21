//------- Client libraries
import { Flow } from '@vaadin/flow-frontend/Flow';
//@ts-ignore
import { Router } from '@vaadin/router';

//------- Theming the app as flow does
import '@vaadin/vaadin-lumo-styles/icons.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/color.js';
//@ts-ignore
import viewStyles from './styles/view-styles.css';
//@ts-ignore
import sharedStyles from './styles/shared-styles.css';
addCssBlock(`<dom-module id="view-styles"><template><style>${viewStyles}</style></template></dom-module>`);
addCssBlock(`<custom-style><style include="view-styles lumo-color lumo-typography">${sharedStyles}</style></custom-style>`);

//------- Application Views
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
                path: '/client-categories',
                title: 'Client Categories',
                component: 'client-categories',
                action: async () => {
                    // FIXME: we should remove import above and use this
                    // but it fails for some reason.
                    //@ts-ignore
                    // await import('./src/client-categories');
                }
            },
            {
                // fallback to Flow if no client-side routes match
                path: '(.*)',
                action: async (cfg: any) => {
                    const view = await flow.navigate(cfg);
                    // FIXME: workaround for https://github.com/vaadin/vaadin-router/issues/357
                    const elem = document.createElement('div');
                    elem.appendChild(view);
                    return elem;
                }
            }
        ]
    }
];

function addCssBlock(block: string) {
    const tpl = document.createElement('template');
    tpl.innerHTML = block;
    document.head.appendChild(tpl.content);
}

const router = new Router(document.querySelector('#outlet'), {});
router.setRoutes(routes);


