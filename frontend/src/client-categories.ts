import {LitElement, html, css, customElement,  property} from 'lit-element';

import '@vaadin/vaadin-crud';
import '@vaadin/vaadin-grid/vaadin-grid-filter-column';

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

    @property() items: Array<any> = [];

    connectedCallback() {
      super.connectedCallback();
      ['Mineral Water', 'Soft Drink', 'Coffee', 'Tea', 'Dairy', 'Cider', 'Beer', 'Wine', 'Other']
        .forEach(name => this.items.push({name: name, beverages: Math.floor(Math.random() * 15)}))
    }

    save(evt: any) {
      evt.detail.item.beverages = 0;
    }
}
