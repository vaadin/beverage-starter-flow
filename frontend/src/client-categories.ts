import {LitElement, html, css, customElement, property} from 'lit-element';

import '@vaadin/vaadin-crud';
import '@vaadin/vaadin-grid/vaadin-grid-filter-column';

import * as CategoryService from '../generated/CategoryService';
import CategoryDTO from '../generated/com/vaadin/starter/beveragebuddy/connect/CategoryService/CategoryDTO';

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

    @property() items: Array<CategoryDTO> = [];

    async connectedCallback() {
      super.connectedCallback();
      const response = await CategoryService.list('') || [];
      this.items = response.filter(c => c !== null) as CategoryDTO[];
    }

    async save(evt: CustomEvent) {
      const category: CategoryDTO = evt.detail.item;
      category.beverages = 0;
      await CategoryService.save(category);
    }

    onBeforeLeave(): any {
      if (this.shadowRoot) {
        const crud = this.shadowRoot.querySelector('vaadin-crud') as any;
        return {cancel: crud && crud.__isDirty};
      }
    }
}
