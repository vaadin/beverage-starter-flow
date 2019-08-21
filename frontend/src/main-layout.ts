import {LitElement, html, customElement, css} from 'lit-element';
import '@polymer/iron-icon';
import '@vaadin/vaadin-icons';

@customElement('main-layout')
export class MainLayout extends LitElement {
    static styles = css`
        :host {
          display: flex;
          flex-direction: column;
          width: 100%;
          height: 100%;
          min-height: 100vh;
          max-width: 960px;
          margin: 0 auto;
        }
        :host ::slotted(*) {
          flex-grow: 1;
          display: flex;
          flex-direction: column;
          margin: 0 20px 20px;
        }
        .title {
          font-size: 1em;
          margin: 0;
          /* Allow the nav-items to take all the space so they are centered */
          width: 0;
          line-height: 1;
          letter-spacing: -0.02em;
          font-weight: 500;
        }

        .header {
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

        .nav {
          display: flex;
          flex: 1;
          justify-content: center;
        }

        .nav-item {
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

        .nav-item:hover {
          text-decoration: none;
        }

        .nav-item:not([highlight]):hover {
          color: inherit;
        }

        .nav-item[highlight] {
          color: var(--lumo-primary-text-color);
          cursor: default;
        }

        .nav-item iron-icon {
          /* Vaadin icons are using a 16x16 grid */
          padding: 4px;
          box-sizing: border-box;
          pointer-events: none;
        }

    `;

    render() {
        return html`
          <div class="header">
            <h2 class="title">Beverage Buddy</h2>
            <div class="nav">
              <!-- do not set route-link attribute, used by flow -->
              <a class="nav-item" href="flow-review-list"><iron-icon icon="vaadin:list"></iron-icon>Reviews</a>
              <a class="nav-item" href="client-categories"><iron-icon icon="vaadin:archives"></iron-icon>Categories</a>
            </div>
          </div>
          <slot></slot>
        `;
    }
}
