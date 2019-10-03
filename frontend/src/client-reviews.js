import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';

import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-button/theme/lumo/vaadin-button.js';
import '@vaadin/vaadin-checkbox/theme/lumo/vaadin-checkbox.js';
import '@vaadin/vaadin-text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/vaadin-grid/theme/lumo/vaadin-grid.js';

import client from '../generated/connect-client.default';

const div = document.createElement('div');
div.innerHTML = '<custom-style><style include="lumo-color lumo-typography"></style></custom-style>';
document.head.insertBefore(div.firstElementChild, document.head.firstChild);

import * as connectServices from '../generated/ConnectServices';

class ClientReviews extends PolymerElement {
  static get template() {
    return html`
    <style>
      vaadin-grid {
      }

      .view-toolbar {
        display: flex;
      }
      .view-toolbar__search-field {
        flex: 1;
      }
    } 
    </style>

    <div class="view-toolbar">
      <vaadin-text-field id="search" on-change="update" class="view-toolbar__search-field" tabindex="0">
        <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
      </vaadin-text-field>

      <vaadin-button class="view-toolbar__button" theme="primary" tabindex="0" role="button">
        <iron-icon icon="lumo:plus" slot="prefix"></iron-icon>New category
      </vaadin-button>
    </div>

    <h3>Reviews</h3>
    <vaadin-grid id="grid">
      <vaadin-grid-column path="name" header="Beverate"></vaadin-grid-column>
      <vaadin-grid-column path="category.name" header="Category Name"></vaadin-grid-column>
      <vaadin-grid-column path="score" header="Ratting"></vaadin-grid-column>
      <vaadin-grid-column path="count" header="Times Tasted"></vaadin-grid-column>
      <vaadin-grid-column path="date" header="Last Time Tasted"></vaadin-grid-column>
    </vaadin-grid>
`;
  }

  static get is() {
    return 'client-reviews';
  }

  ready() {
    super.ready();
    this.update();
  }

  update() {
    const search = this.shadowRoot.querySelector(('#search'));
    connectServices.reviews(search.value).then(items => this.$.grid.items = items);
  }
}

window.customElements.define(ClientReviews.is, ClientReviews);
