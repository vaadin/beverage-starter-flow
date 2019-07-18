const {log, LogCategory} = require('@vaadin/connect-scripts/lib/log');

const webpack = require('webpack');

const {BabelMultiTargetPlugin} = require('webpack-babel-multi-target-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackIncludeAssetsPlugin = require('html-webpack-include-assets-plugin');

const path = require('path');

const inputDir = './frontend';

// This directory is served as static in a spring-boot installation
const outputDir = './target/classes/META-INF/resources';

module.exports = (env, argv) => {
  return {

    // Default build mode (the frontend development server forces 'development')
    mode: 'production',

    // Include source maps in the build
    devtool: 'source-map',

    // The directory with the frontend sources
    context: path.resolve(process.cwd(), inputDir),

    entry: {
      polyfills: './polyfills.ts',
      index: './index.ts'
    },

    resolve: {
      // Enable resolving .ts imports files without extensions
      extensions: [
        '.ts',
        '.js'
      ],

      // Prefer ES module dependencies when declared in package.json
      mainFields: [
        'es2015',
        'module',
        'main'
      ]
    },

    module: {
      rules: [
        {
          test: /\.[jt]s$/,
          use: [
            BabelMultiTargetPlugin.loader(),
            'uglify-template-string-loader'
          ],
        },
        {
          test: /\.ts$/,
          use: [
            {
              loader: 'awesome-typescript-loader',
              options: {
                silent: true,
                useCache: true,
                cacheDirectory: 'node_modules/.cache/awesome-typescript-loader',
              },
            }
          ],
        }
      ]
    },

    output: {
      filename: '[name].js',
      path: path.resolve(process.cwd(), outputDir)
    },

    performance: {
      maxAssetSize: 500000,
      maxEntrypointSize: 500000
    },

    plugins: [

      new CopyWebpackPlugin([
        // Copy static assets
        {
          from: '**/*',
          context: path.resolve('static')
        },
        // Copy @webcomponents/webcomponentsjs
        {
          from: 'webcomponentsjs/**/*',
          context: path.resolve(require.resolve(
            '@webcomponents/webcomponentsjs/package.json'
          ), '../..')
        }
      ]),

      // Provide regeneratorRuntime for Babel async transforms
      new webpack.ProvidePlugin({
        regeneratorRuntime: 'regenerator-runtime'
      }),

      // Babel configuration for multiple output bundles targeting different sets
      // of browsers
      new BabelMultiTargetPlugin({
        babel: {
          plugins: [
            [
              require('babel-plugin-template-html-minifier'),
              {
                modules: {
                  '@polymer/polymer/lib/utils/html-tag.js': ['html']
                },
                htmlMinifier: {
                  collapseWhitespace: true,
                  minifyCSS: true,
                  removeComments: true
                }
              }
            ]
          ],

          // @babel/preset-env options common for all bundles
          presetOptions: {
            // debug: true, // uncomment to debug the babel configuration

            // Don’t add polyfills, they are provided from webcomponents-loader.js
            useBuiltIns: false
          }
        },

        // Modules excluded from targeting into different bundles
        doNotTarget: [
          // Array of RegExp patterns
        ],

        // Modules that should not be transpiled
        exclude: [
          // Array of RegExp patterns
        ],

        // Target browsers with and without ES modules support
        targets: {
          modern: {
            browsers: [
              'last 2 Chrome major versions',
              'last 2 ChromeAndroid major versions',
              'last 2 Edge major versions',
              'last 2 Firefox major versions',
              'last 2 Safari major versions',
              'last 2 iOS major versions'
            ],
            key: 'es6',
            tagAssetsWithKey: false, // don’t append a suffix to the file name
            esModule: true // marks the bundle used with <script type="module">
          },
          legacy: {
            browsers: [
              'ie 11'
            ],
            key: 'es5',
            tagAssetsWithKey: true, // append a suffix to the file name
            noModule: true // marks the bundle included without `type="module"`
          }
        }
      }),

      // Insert the bundles in the html file
      new HtmlWebpackPlugin({
        template: 'index.html',

        // Prevent adding multiple bunldles for polyfills, browsers that have ES
        // modules support don’t need them. The polyfills are listed directly in
        // the html template to ensure correct loading order.
        excludeChunks: ['polyfills']
      }),

      // Append Spring Boot LiveReload script in development mode
      argv.mode === 'development' && new HtmlWebpackIncludeAssetsPlugin({
        assets: ['http://localhost:35729/livereload.js'],
        append: true,
        resolvePaths: false,
        publicPath: false
      }),

      // When Webpack finishes, show a message when in devmode
      function() {
        if (argv.mode === 'development') {
          let first = true;
          // eslint-disable-next-line no-invalid-this
          this.hooks.done.tap('VaadinConnect', stats => {
            setTimeout(() => {
              if (stats.hasErrors()) {
                log(
                  LogCategory.Error,
                  'There are compilation errors in the frontend code'
                );
              } else {
                if (first) {
                  first = false;
                  if (argv.watch) {
                    log(
                      LogCategory.Progress,
                      `Webpack is watching or changes on ${inputDir}`
                    );
                  }
                  if (process.env.CONNECT_BACKEND) {
                    log(
                      LogCategory.Success,
                      'The application is ready at: \x1b[32m',
                      process.env.CONNECT_BACKEND,
                      '\x1b[0m 👍'
                    );
                  }
                } else {
                  log(LogCategory.Progress, 'Webpack has compiled the changes');
                }
              }
            });
          });
        }
      }

    ].filter(Boolean)

  };
};
