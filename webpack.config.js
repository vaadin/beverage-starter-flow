const path = require('path');
const fs = require('fs');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const {BabelMultiTargetPlugin} = require('webpack-babel-multi-target-plugin');


const inputFolder = path.resolve(__dirname, './src/main/webapp/frontend');
const outputFolder = path.resolve(__dirname, './src/main/webapp/');
const statsFolder = path.resolve(__dirname, './target/classes/META-INF/resources');
fs.mkdirSync(statsFolder, { recursive: true });
const statsFile = statsFolder + '/stats.json';

module.exports = {
  context: path.resolve(__dirname, inputFolder),
  entry: './main.js',
  mode: 'production',

  output: {
    filename: 'build/[name].js',
    path: path.resolve(__dirname, outputFolder)
  },

  module: {
    rules: [{
      test: /\.js$/,
      use: [BabelMultiTargetPlugin.loader(), 'uglify-template-string-loader']
    }]
  },

  plugins: [
    function (compiler) {
      compiler.plugin('after-emit', function (compilation, done) {
        console.log("Emitted " + statsFile)
        fs.writeFile(path.resolve(__dirname, statsFile),
          JSON.stringify(compilation.getStats().toJson(), null, 2), done);
      });
    },
    new CopyWebpackPlugin(
      ['webcomponentsjs/**/*'],
      { context: path.resolve(__dirname, 'node_modules', '@webcomponents') }
    ),
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
      // Target browsers with and without ES modules support
      targets: {
        'es6': {
          browsers: [
            'last 2 Chrome major versions',
            'last 2 ChromeAndroid major versions',
            'last 2 Edge major versions',
            'last 2 Firefox major versions',
            'last 2 Safari major versions',
            'last 2 iOS major versions'
          ],
          tagAssetsWithKey: false, // don’t append a suffix to the file name
          esModule: true // marks the bundle used with <script type="module">
        },
        'es5': {
          browsers: [
            'ie 11'
          ],
          tagAssetsWithKey: true, // append a suffix to the file name
          noModule: true // marks the bundle included without `type="module"`
        }
      }
    }),         
  ]
};
