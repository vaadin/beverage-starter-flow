import {LitElement, html, customElement, css, unsafeCSS, CSSResult} from 'lit-element';
import '@polymer/iron-icon';
import '@vaadin/vaadin-icons';

// FIXME: import .css files without @ts-ignore (https://github.com/vaadin/flow/issues/6335)
//@ts-ignore
import mainLayoutStyles from '../styles/main-layout-styles.css';
const styles : CSSResult = unsafeCSS(mainLayoutStyles);

@customElement('main-layout')
export class MainLayout extends LitElement {

    static styles = css`${styles}`;

    render() {
        return html`
          <div class="main-layout__header">
            <h2 class="main-layout__title">Beverage Buddy</h2>
            <div class="main-layout__nav">
              <a class="main-layout__nav-item" href=""><iron-icon icon="vaadin:list"></iron-icon>Reviews</a>
              <a class="main-layout__nav-item" href="categories"><iron-icon icon="vaadin:archives"></iron-icon>Categories</a>
              <a class="main-layout__nav-item" href="client-reviews"><iron-icon icon="vaadin:list"></iron-icon>Connect-Reviews</a>
              <a class="main-layout__nav-item" href="client-categories"><iron-icon icon="vaadin:archives"></iron-icon>Connect-Categories</a>
            </div>
          </div>
          <slot></slot>
        `;
    }
}
