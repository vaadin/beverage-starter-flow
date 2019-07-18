import {LitElement, customElement, html, property, query} from 'lit-element';

import {LoginViewElement} from './login-view';

import './login-view';
import './status-view';

@customElement('app-view')
export class AppViewElement extends LitElement {
  @property({type: Boolean})
  loggedIn: boolean = false;

  @query('login-view')
  loginViewElement?: LoginViewElement;

  createRenderRoot() {return this;}

  render() {
    return html`
      ${!this.loggedIn
        ? html`<login-view></login-view>`
        : html`<status-view></status-view>`
      }
    `;
  }

  async firstUpdated(): Promise<void> {
    await customElements.whenDefined('login-view');
    await (this.loginViewElement as LoginViewElement).ensureLoggedIn();
    this.loggedIn = true;
  }
}
