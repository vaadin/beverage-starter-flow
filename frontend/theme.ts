import '@vaadin/vaadin-lumo-styles/color.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/icons.js'
import '@vaadin/vaadin-lumo-styles/badge.js'

// FIXME: import .css files without @ts-ignore (https://github.com/vaadin/flow/issues/6335)
//@ts-ignore
import viewStyles from './styles/view-styles.css';
//@ts-ignore
import sharedStyles from './styles/shared-styles.css';

function addCssBlock(block: string) {
  const tpl = document.createElement('template');
  tpl.innerHTML = block;
  document.head.appendChild(tpl.content);
}

addCssBlock(`<dom-module id="view-styles">
  <template>
    <style>
        ${viewStyles}
    </style>
  </template>
</dom-module>`);

addCssBlock(`<custom-style>
    <style include="view-styles lumo-color lumo-typography">
        ${sharedStyles}
    </style>
</custom-style>`);
