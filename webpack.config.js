const path = require('path');
const fs = require('fs');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const NpmInstallPlugin = require('webpack-plugin-install-deps');

const inputFolder = path.resolve(__dirname, './src/main/webapp/frontend');
const outputFolder = path.resolve(__dirname, './src/main/webapp/');
const statsFolder = path.resolve(__dirname, './target/classes/META-INF/resources');
fs.mkdirSync(statsFolder, { recursive: true });
const statsFile = statsFolder + '/stats.json';

module.exports = {
  mode: 'production',
  context: inputFolder,
  entry: {
    index: './main.js'
  },

  output: {
    filename: 'build/[name].js',
    path: outputFolder
  },

  plugins: [

    // Automatically execute `npm install` to download & install missing dependencies.
    new NpmInstallPlugin(),

    // Generates the `stats.json` file which is used by flow to read templates for
    // server `@Id` binding
    function (compiler) {
      compiler.plugin('after-emit', function (compilation, done) {
        console.log("Emitted " + statsFile)
        fs.writeFile(path.resolve(__dirname, statsFile),
          JSON.stringify(compilation.getStats().toJson(), null, 2), done);
      });
    },

    // Copy webcomponents polyfills. They are not bundled because they
    // have its own loader based on browser quirks.
    new CopyWebpackPlugin(
      ['webcomponentsjs/**/*'],
      { context: path.resolve(__dirname, 'node_modules', '@webcomponents') }
    ),
  ]
};
