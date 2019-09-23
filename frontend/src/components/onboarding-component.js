/*
 ~ Copyright 2000-2017 Vaadin Ltd.
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License"); you may not
 ~ use this file except in compliance with the License. You may obtain a copy of
 ~ the License at
 ~
 ~ http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 ~ WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 ~ License for the specific language governing permissions and limitations under
 ~ the License.
 */
import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import {GestureEventListeners} from '@polymer/polymer/lib/mixins/gesture-event-listeners.js';
import '@vaadin/vaadin-dialog/vaadin-dialog.js';

const unsafeHtmlLiteral = function(value) {
    const template = document.createElement('template');
    template.innerHTML = value;
    return template;
};

const mobileWidthPx = 500;
const phoneMedia = `(max-width: ${mobileWidthPx}px), (max-height: ${mobileWidthPx}px)`;

class OnBoardingComponent extends GestureEventListeners(PolymerElement) {

    static get properties() {
        return {
            activeSlide:  {
                type: Number,
                value: 1,
                observer: 'onActiveSlideChange'
            }
        }
    }

    static get template() {
        return html`
            <vaadin-dialog id="dialog" theme="full-screen-on-mobile" opened no-close-on-outside-click no-close-on-esc on-opened-changed="onOpenedChanged">
                <template>
                    <style>
                        .cta-wrap {
                            max-width: ${unsafeHtmlLiteral(mobileWidthPx)}px;
                            margin: 0 auto;
                            height: 100%;
                            position: relative;
                            overflow: hidden;
                            display: flex;
                            flex-direction: column;
                            background-color: #fff;
                            color: var(--lumo-shade-90pct);
                            border-radius: var(--lumo-border-radius-m);
                            transform: translateZ(0);
                        }

                        @media ${unsafeHtmlLiteral(phoneMedia)} {
                            .cta-wrap {
                                border-radius: 0;
                            }
                        }

                        .cta-wrap::before {
                            content: "";
                            position: absolute;
                            top: -1px;
                            left: 0;
                            width: 130%;
                            height: 50%;
                            background-image: url("images/onboard-bg-full.svg");
                            background-size: cover;
                            background-repeat: no-repeat;
                            background-position: 0 100%;
                            transition: transform 1.5s;
                            transform: translate(0%, 0%) rotate(0deg);
                            transform-origin: 0% 100%;
                        }

                        .cta-wrap.slide-2-active::before {
                            transform: translate(-7%, 0%) rotate(-3deg);
                        }

                        .cta-wrap.slide-3-active::before {
                            transform: translate(-10%, 0%) rotate(-6deg);
                        }

                        .cta-content {
                            text-align: center;
                            display: flex;
                            flex-direction: column;
                            max-height: 75vh;
                        }

                        .logo-box {
                            margin-top: 20%;
                            height: 40px;
                            text-align: center;
                            width: 100%;
                            font-size: 3em;
                            font-weight: bold;
                            color: white;
                        }

                        .logo-box img {
                            transform: translateX(60%);
                            transition: all 1s;
                            will-change: transform;
                            height: 100%;
                        }

                        .illustration-box {
                            margin-top: 7vh;
                            transform: translateX(60%);
                            transition: all 1.25s;
                            position: relative;
                            will-change: transform;
                        }

                        .illustration-box .img-alt {
                            position: absolute;
                            left: 0;
                            right: 0;
                            top: 0;
                            max-width: 100%;
                            margin: auto;
                        }

                        .text-box {
                            flex-grow: 1;
                            display: flex;
                            flex-direction: column;
                            justify-content: center;
                            transform: translateX(80%);
                            transition: all 1.25s;
                            will-change: transform;
                        }

                        .image-box {
                            flex-grow: 0;
                        }

                        .slide .image-box > img {
                            opacity: 0;
                        }

                        .slide {
                            transition: all .5s;
                            text-align: center;
                            position: relative;
                            display: flex;
                            flex-direction: column;
                            width: 100%;
                            height: 100%;
                            will-change: transform;
                        }

                        .slide-1, .slide-3-active .slide-2 {
                            transform: translateX(-100%);
                        }

                        .slide-2, .slide-3 {
                            transform: translateX(100%);
                        }

                        .slide-2, .slide-3 {
                            position: absolute;
                            top: 0;
                            left: 0;
                            width: 100%;
                        }

                        .slide-2 .logo-box img {
                             height: 150%;
                             margin-top: -2%;
                        }

                        .slide-1-active .slide-1,
                        .slide-1-active .slide-1 .illustration-box,
                        .slide-1-active .slide-1 .logo-box img,
                        .slide-1-active .slide-1 .text-box,
                        .slide-2-active .slide-2,
                        .slide-2-active .slide-2 .illustration-box,
                        .slide-2-active .slide-2 .logo-box img,
                        .slide-2-active .slide-2 .text-box,
                        .slide-3-active .slide-3,
                        .slide-3-active .slide-3 .illustration-box,
                        .slide-3-active .slide-3 .logo-box img,
                        .slide-3-active .slide-3 .text-box {
                            transform: translateX(0);
                        }

                        .slide-2-active .slide-1 .logo-box img,
                        .slide-3-active .slide-1 .logo-box img,
                        .slide-3-active .slide-2 .logo-box img {
                            transform: translateX(-40%);
                        }

                        .slide-2-active .slide-1 .illustration-box,
                        .slide-3-active .slide-1 .illustration-box,
                        .slide-3-active .slide-2 .illustration-box {
                            transform: translateX(-60%);
                        }

                        .slide-2-active .slide-1 .text-box,
                        .slide-3-active .slide-1 .text-box,
                        .slide-3-active .slide-2 .text-box {
                            transform: translateX(-80%);
                        }

                        .onboard-title {
                            font-weight: 600;
                            font-size: 18px;
                            margin: 0;
                        }

                        .text-content p {
                            padding: 0 30px;
                            font-size: 14px;
                            max-width: 380px;
                            margin-left: auto;
                            margin-right: auto;
                        }

                        .text-content p a {
                            color: var(--lumo-primary-text-color);
                            font-weight: 600;
                            text-decoration: none;
                        }

                        .onboard-btn {
                            width: 100%;
                        }

                        .indicator {
                            text-align: center;
                            position: relative;
                        }

                        .indicator span {
                            width: 8px;
                            height: 8px;
                            margin: 0 5px;
                            border-radius: 50%;
                            background-color: var(--lumo-shade-30pct);
                            display: inline-block;
                            transition: all .25s;
                        }

                        .slide-1-active .indicator span:first-child,
                        .slide-2-active .indicator span:nth-child(2),
                        .slide-3-active .indicator span:last-child {
                            background-color: var(--lumo-primary-color);
                        }

                        .slide-group {
                            flex-grow: 1;
                            border-radius: var(--lumo-border-radius-m) var(--lumo-border-radius-m) 0 0;
                            overflow: hidden;
                            position: relative;
                            z-index: 1;
                        }

                        .onboard-footer {
                            padding: 20px;
                        }

                        .onboard-footer .button-group {
                            position: relative;
                        }

                        .onboard-footer .button-group .onboard-btn:not(:first-child) {
                            position: absolute;
                            top: 0;
                            left: 0;
                            width: 100%;
                        }

                        .onboard-footer .button-group .onboard-btn {
                            opacity: 0;
                            pointer-events: none;
                            will-change: opacity;
                            /* Clone vaadin-button styles*/
                            margin: var(--lumo-space-xs) 0;
                            padding: 0 var(--lumo-space-m);
                            height: 2em;
                            border-radius: var(--lumo-border-radius-m);
                            background-color: var(--lumo-primary-color);
                            color: var(--lumo-primary-contrast-color);
                            font-family: var(--lumo-font-family);
                            font-weight: 600;
                            font-size: var(--lumo-font-size-l);
                            border: none;
                            outline: none;
                        }

                        .onboard-btn:focus {
                            box-shadow: 0 0 0 2px #fff, 0 0 0 4px var(--lumo-primary-color-50pct);
                        }

                        .slide-1-active .onboard-footer .button-group .for-slide-1,
                        .slide-2-active .onboard-footer .button-group .for-slide-2,
                        .slide-3-active .onboard-footer .button-group .for-slide-3 {
                            opacity: 1;
                            pointer-events: auto;
                        }

                        .qr-code {
                            width: 65px;
                            height: 65px;
                            margin: 0.5em 1em 0.5em 0;
                            vertical-align: middle;
                        }

                        .qr-code + span {
                            display: inline-block;
                            text-align: left;
                            vertical-align: middle;
                        }

                        @media only screen and (max-height: 680px) {
                            .logo-box {
                                height: 30px;
                                margin-top: 7vh;
                            }

                            .illustration-box img {
                                max-width: 180px;
                            }

                            .onboard-footer {
                                flex-grow: 1;
                                justify-content: flex-end;
                                display: flex;
                                flex-direction: column;
                                padding-bottom: 10px;
                            }
                        }

                        @media only screen and (max-height: 520px) {
                            .illustration-box img {
                                max-width: 120px;
                            }

                            .slide-1, .slide-2, .slide-3, .cta-wrap {
                                background-position-y: -110px;
                            }
                        }

                        @media only screen and (max-width: 860px) {
                            .hide-on-mobile {
                                display: none;
                            }
                        }


                        @media only screen and (min-width: 861px) {
                            .hide-on-desktop {
                                display: none;
                            }

                            .text-box {
                                flex-basis: 200px;
                            }
                        }

                    </style>
                    <div id="slidercage" class="cta-wrap slide-1-active" on-track="handleTrack">
                        <div class="slide-group">
                            <div class="slide slide-1">
                                <div class="logo-box">
                                    <img src="images/onboard-vaadin-logo.svg" alt="Vaadin Logo">
                                </div>
                                <div class="image-box">
                                    <div class="illustration-box">
                                        <img src="images/onboarding-1.svg">
                                    </div>
                                </div>
                                <div class="text-box">
                                    <h3 class="onboard-title">Welcome to the demo</h3>
                                    <div class="text-content">
                                        <p>
                                            Beverage Buddy is a demo app for the <a href="https://vaadin.com/">Vaadin platform</a>, showcasing its capabilities.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="slide slide-2">
                                <div class="logo-box">
                                    <img src="images/onboard-pwa-logo-white.svg" alt="PWA Logo">
                                </div>
                                <div class="image-box">
                                    <div class="illustration-box">
                                        <img src="images/onboarding-2.svg">
                                    </div>
                                </div>
                                <div class="text-box">
                                    <h3 class="onboard-title">Progressive Web App</h3>
                                    <div class="text-content">
                                        <p>
                                          Beverage Buddy is a <a href="https://vaadin.com/pwa">Progressive Web App</a>. That means it works great on any device and that you can install it on your devices so that it works like a native app.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="slide slide-3">
                                <div class="logo-box">
                                    Beverage
                                    <!-- <img src="images/onboard-taskmob-white-logo.svg" alt="TaskMob Logo"> -->
                                </div>
                                <div class="image-box">
                                    <div class="illustration-box">
                                        <img src="images/onboarding-3.svg">
                                    </div>
                                </div>
                                <div class="text-box">
                                    <h3 class="onboard-title">That’s it!</h3>
                                    <div class="text-content">
                                        <p class="text-content">You’re ready to start using Beverage Buddy.<br>By using the app, you agree to our <a href="https://vaadin.com/privacy-policy" target="_blank">cookie policy</a>.</p>
                                        <p class="text-content hide-on-mobile">
                                            <img class="hide-on-mobile qr-code" src="images/qr-code.svg">
                                            <span>Don’t forget to try<br>the app on your phone.</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="onboard-footer">
                            <div class="button-group">
                                <button class="onboard-btn for-slide-1" on-click="onSlide1Next">Okay, let’s continue</button>
                                <button class="onboard-btn for-slide-2" on-click="onSlide2Next">Next</button>
                                <button class="onboard-btn for-slide-3" on-click="closeDialog">Let’s go!</button>
                            </div>
                            <div class="indicator">
                                <span on-click="slide1Active"></span>
                                <span on-click="slide2Active"></span>
                                <span on-click="slide3Active"></span>
                            </div>
                        <div>
                    </div>
                </template>
            </vaadin-dialog>
        `;
    }

    openDialog() {
        this.$.dialog.opened = true;
        this.updateTabIndexes();
    }

    closeDialog() {
        this.$.dialog.opened = false;
        this.dispatchEvent(new CustomEvent('done'));
    }

    onOpenedChanged(e) {
        if (e.detail.value) {
            document.documentElement.classList.add('onboarding');
        } else {
            document.documentElement.classList.remove('onboarding');
            this.dispatchEvent(new CustomEvent('onboarding-finished'));
        }
    }

    handleTrack(e) {
        switch(e.detail.state) {
            case 'start':
                this.trackStart = {
                    x: e.detail.x,
                    y: e.detail.y
                };
                break;
            case 'end':
                if (this.trackStart) {
                    const deltaX = e.detail.x - this.trackStart.x;
                    const deltaY = e.detail.y - this.trackStart.y;

                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (deltaX > 0 && this.activeSlide > 1) {
                            this.activeSlide = this.activeSlide - 1;
                        } else if (deltaX < 0 && this.activeSlide < 3) {
                            this.activeSlide = this.activeSlide + 1;
                        }
                    }

                    this.trackStart = undefined;
                }
                break;
        }
    }

    onSlide1Next() {
        this.activeSlide = 2;
    }

    onSlide2Next() {
        this.activeSlide = 3;
    }

    slide1Active() {
        this.activeSlide = 1;
    }

    slide2Active() {
        this.activeSlide = 2;
    }

    slide3Active() {
        this.activeSlide = 3;
    }

    onActiveSlideChange(slide) {
        const slidercage = document.querySelector('vaadin-dialog-overlay')
            .shadowRoot.querySelector('#content')
            .shadowRoot.querySelector('#slidercage');

        slidercage.classList.remove('slide-1-active', 'slide-2-active', 'slide-3-active');
        slidercage.classList.add(`slide-${slide}-active`);

        this.updateTabIndexes(slide);
    }

    updateTabIndexes(activeSlide) {
        const slidercage = document.querySelector('vaadin-dialog-overlay')
            .shadowRoot.querySelector('#content')
            .shadowRoot.querySelector('#slidercage');
        Array.from(slidercage.querySelectorAll('button, a[href]')).forEach(el => {
            if (this._buttonOrLinkForSlide(slidercage, el, activeSlide)) {
                el.removeAttribute('tabindex');
            } else {
                el.setAttribute('tabindex', '-1');
            }
        });
    }

    _buttonOrLinkForSlide(slidercage, el, slideNum) {
        return slidercage.classList.contains(`slide-${slideNum}-active`) && (el.classList.contains('for-slide-' + slideNum) || el.closest('.slide-' + slideNum));
    }
}

customElements.define('onboarding-component', OnBoardingComponent);
