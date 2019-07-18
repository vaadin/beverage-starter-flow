import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-grid/vaadin-grid.js';

import { html } from '@polymer/polymer/lib/utils/html-tag.js';
import client from './generated/connect-client.default';

import '@vaadin/vaadin-lumo-styles/icons.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/color.js';

const div = document.createElement('div');
div.innerHTML = '<custom-style><style include="lumo-color lumo-typography"></style></custom-style>';
document.head.insertBefore(div.firstElementChild, document.head.firstChild);


import * as connectServices from './generated/ConnectServices';

class SearchBar extends PolymerElement {
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

      .main-layout {
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        min-height: 100vh;
        max-width: 960px;
        margin: 0 auto;
    }

    .main-layout__title {
        font-size: 1em;
        margin: 0;
        /* Allow the nav-items to take all the space so they are centered */
        width: 0;
        line-height: 1;
        letter-spacing: -0.02em;
        font-weight: 500;
    }

    vaadin-vertical-layout, vaadin-grid {
        flex: auto;
    }

    .main-layout__header {
        display: flex;
        flex: none;
        align-items: center;
        height: var(--main-layout-header-height);

        /* Stretch to fill the entire browser viewport, while keeping the content constrained to
           parent element max-width */
        margin: 0 calc(-50vw + 50%);
        padding: 0 calc(50vw - 50% + 16px);

        background-color: var(--lumo-base-color);
        box-shadow: 0 1px 0 0 var(--lumo-contrast-5pct);
    }

    .main-layout__nav {
        display: flex;
        flex: 1;
        justify-content: center;
    }

    .main-layout__nav-item {
        display: inline-flex;
        flex-direction: column;
        align-items: center;
        padding: 4px 8px;
        cursor: pointer;
        transition: 0.3s color, 0.3s transform;
        will-change: transform;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        font-size: var(--lumo-font-size-s);
        color: var(--lumo-secondary-text-color);
        font-weight: 500;
        line-height: 1.3;
    }

    .main-layout__nav-item:hover {
        text-decoration: none;
    }

    .main-layout__nav-item:not([highlight]):hover {
        color: inherit;
    }

    .main-layout__nav-item[highlight] {
        color: var(--lumo-primary-text-color);
        cursor: default;
    }

    .main-layout__nav-item iron-icon {
        /* Vaadin icons are using a 16x16 grid */
        padding: 4px;
        box-sizing: border-box;
        pointer-events: none;
    }
    </style>

    <div class="main-layout__header">
      <h2 class="main-layout__title">Beverage Buddy</h2>
      <div class="main-layout__nav">
        <a class="main-layout__nav-item" highlight="" router-link="" href="flow/">
          <iron-icon icon="vaadin:list"></iron-icon>Reviews</a>
        <a class="main-layout__nav-item" router-link="" href="flow/categories">
          <iron-icon icon="vaadin:archives"></iron-icon>Categories</a>
        <div class="main-layout__nav-item">
          <iron-icon icon="vaadin:archives"></iron-icon>Connect Categories</div>
      </div>
    </div>

    <div class="view-toolbar">
      <vaadin-text-field id="search" on-change="update" class="view-toolbar__search-field" tabindex="0">
        <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
      </vaadin-text-field>

      <vaadin-button class="view-toolbar__button" theme="primary" tabindex="0" role="button">
        <iron-icon icon="lumo:plus" slot="prefix"></iron-icon>New category
      </vaadin-button>
    </div>

    <h3>Categories</h3>
    <vaadin-grid id="grid">
      <vaadin-grid-column path="name" header="Name"></vaadin-grid-column>
      <vaadin-grid-column path="id" header="Beverages"></vaadin-grid-column>
    </vaadin-grid>
`;
  }

  static get is() {
    return 'connect-categories';
  }

  ready() {
    super.ready();
    this.update();
  }

  update() {
    connectServices.categories(this.$.search.value).then(items => this.$.grid.items = items);
  }
}

window.customElements.define(SearchBar.is, SearchBar);
