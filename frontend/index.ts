// Add an onboarding dialog into the body, and return a promise
// that resolves when onboarding is done.
async function showOnboarding() {
  return new Promise(resolve => {
    const onboarding = document.createElement('onboarding-component');
    onboarding.addEventListener('onboarding-done', resolve);
    document.body.appendChild(onboarding);
    return onboarding;
  });
}

(async () => {
  // Show an onboarding dialog before loading the full Flow app.
  // Skip onboarding if already done in this browser, or if explicitly requested to.
  const onboardingDone = window.localStorage.getItem('onboarding-done');
  const skipOnboarding = window.location.search.indexOf('skip-onboarding') > -1;
  if (!onboardingDone && !skipOnboarding) {
    // Initiate a dynamic import, but do not wait for it to append the onboarding element into DOM
    import(/* webpackChunkName: "onboarding-component" */ './src/components/onboarding-component');
    await showOnboarding();
    localStorage.setItem('onboarding-done', 'true');
  }

  // After onboarding is done, load Flow and let it take control over the page
  const {Flow} = await import(/* webpackChunkName: "flow" */ '@vaadin/flow-frontend/Flow');
  const flow = new Flow({
    imports: () => import(
      // @ts-ignore
      /* webpackChunkName: "generated-flow-imports" */ '../target/frontend/generated-flow-imports')
  });

  // Use this way to start Flow when client-side routes are not needed
  await flow.start();

  // If client-side routes are needed, Flow should be used together with Vaadin Router:
  // const {Router} = await import(/* webpackChunkName: "router" */ '@vaadin/router');
  // const router = new Router(document.body);
  // router.setRoutes([flow.route]);
})();
