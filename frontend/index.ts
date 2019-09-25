import {Flow} from '@vaadin/flow-frontend/Flow';

const flow = new Flow({
  //@ts-ignore
  imports: () => import('../target/frontend/generated-flow-imports')
});

function loadFlow() : void {
  localStorage.setItem('on-boarding-done', 'true');
  flow.start();
}

async function loadOnBoarding() {
  await import('./src/components/onboarding-component');
  const boarding = document.createElement('onboarding-component');
  document.body.appendChild(boarding);
  boarding.addEventListener('done', loadFlow);
}

if (!window.localStorage.getItem('on-boarding-done')) {
  loadOnBoarding();
} else {
  loadFlow();
}


