import {LitElement, html, css, customElement, property} from 'lit-element';

import '@vaadin/vaadin-crud';
import '@vaadin/vaadin-grid/vaadin-grid-filter-column';
import * as connectServices from '../generated/ConnectServices';

@customElement('client-categories')
export class ClientCategories extends LitElement {
    static styles = css`
      vaadin-crud {
        margin-top: 2em;
      }
    `;

    render() {
        return html`
          <vaadin-crud .items="${this.items}" @save="${this.save}">
            <vaadin-form-layout slot="form">
              <vaadin-text-field path="name" label="Name" required></vaadin-text-field>
            </vaadin-form-layout>
            <vaadin-grid slot="grid" height-by-rows>
              <vaadin-grid-filter-column path="name" ></vaadin-grid-filter-column>
              <vaadin-grid-column path="beverages" width="7em" flex-grow="0"></vaadin-grid-column>
              <vaadin-crud-edit-column></vaadin-crud-edit-column>
            </vaadin-grid>
          </vaadin-crud>
        `;
    }

    @property() items: Array<any>|null = [];

    connectedCallback() {
      super.connectedCallback();
      connectServices.categories('').then(items => this.items = items);
    }

    save(evt: any) {
      evt.detail.item.beverages = 0;
      connectServices.save(evt.detail.item).then(items => {
        this.items = items
        this.crud.__isDirty = false;
      });
    }

    onBeforeLeave(): any {
      if (this.shadowRoot) {
        return {cancel: this.crud && this.crud.__isDirty};
      }
    }

    get crud() {
      return this.shadowRoot && this.shadowRoot.querySelector('vaadin-crud') as any;
    }
}
