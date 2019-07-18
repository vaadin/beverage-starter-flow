import {LitElement, customElement, html, property, query} from 'lit-element';

import '@vaadin/vaadin-button/vaadin-button.js';
import '@vaadin/vaadin-text-field/vaadin-text-field.js';

import {StatusController} from './status-controller';

@customElement('status-view')
export class StatusViewElement extends LitElement {
  private statusController = new StatusController(
    this.setStatus.bind(this)
  );

  @property({type: String}) status: string = '';
  @query('#newStatusInput') newStatusInput?: HTMLInputElement;

  createRenderRoot() {return this;}

  render() {
    return html`
      <vaadin-text-field
        id="newStatusInput"
        label="New status"
      ></vaadin-text-field>
      &#32;
      <vaadin-button
        id="update"
        @click="${this.onUpdateClick}"
      >Update status</vaadin-button>
      <br>
      <label id="statusLabel">${this.status}</label>
    `;
  }

  setStatus(status: string): void {
    this.status = status;
  }

  async onUpdateClick(): Promise<void> {
    await this.statusController.updateAction(
      this.newStatusInput!.value
    );
  }
}
