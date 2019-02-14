const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const inputFolder = './src/main/webapp/frontend';
const outputFolder = './src/main/webapp/build';

module.exports = {
  context: path.resolve(__dirname, inputFolder),
  entry: './main.js',

  output: {
    filename: 'index.js',
    path: path.resolve(__dirname, outputFolder)
  },

  plugins: [
    new CopyWebpackPlugin(
      ['webcomponentsjs/**/*'],
      {context: path.resolve(__dirname, 'node_modules', '@webcomponents')}
    ),
  ]
};
