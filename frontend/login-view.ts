import {LitElement, html, customElement, property} from 'lit-element';

import '@vaadin/vaadin-login/vaadin-login-overlay.js';

import client from './generated/connect-client.default';

import {
  Credentials,
  CredentialsCallback
} from '@vaadin/connect';

/**
 * A login form view with <vaadin-login-overlay>
 */
@customElement('login-view')
export class LoginViewElement extends LitElement {
  @property({type: Boolean})
  opened: boolean = false;

  @property({type: Boolean})
  disabled: boolean = false;

  @property({type: String})
  errorMessage: string = '';

  @property({type: Object})
  i18n = {
    header: {
      title: 'Vaadin Connect starter',
    },
    form: {
      title: 'Authenticate',
      username: 'Username',
      password: 'Password',
      submit: 'Submit'
    },
    additionalInformation: '(type user / user to authenticate)'
  };

  createRenderRoot() {return this;}

  render() {
    return html`
      <vaadin-login-overlay
        .disabled="${this.disabled}"
        .i18n="${this.i18n}"
        .opened="${this.opened}"
        .error="${Boolean(this.errorMessage)}"
      ></vaadin-login-overlay>
    `;
  }

  private credentialsCallback: CredentialsCallback =
    async(options): Promise<Credentials> => {
      this.errorMessage = (options && options.message) || '';
      this.i18n = Object.assign({}, this.i18n, {errorMessage: {
        title: 'Error',
        message: this.errorMessage
      }});
      this.opened = true;
      this.disabled = false;
      return new Promise(resolve => {
        this.addEventListener('login', ((e: CustomEvent) => {
          this.errorMessage = '';
          this.disabled = true;
          const {username, password} = e.detail;
          resolve({username, password, stayLoggedIn: true});
        }) as EventListener, {once: true});
      });
    };

  async ensureLoggedIn(): Promise<void> {
    client.credentials = this.credentialsCallback.bind(this);
    while (!client.token) {
      await client.login();
    }
    this.opened = false;
  }
}
