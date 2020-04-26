module.exports = {
  // Tailwind Paths
  configJS: 'tailwind.config.js',
  sourceCSS: 'src/main/webapp/content/css/tailwind.css',
  outputCSS: 'src/main/webapp/content/css/styles.css',
  // Sass
  sass: false,
  // PurgeCSS Settings
  purge: false,
  keyframes: false,
  fontFace: false,
  rejected: false,
  whitelist: [],
  whitelistPatterns: [],
  whitelistPatternsChildren: [],
  extensions: [
    '.ts',
    '.html',
    '.js'
  ],
  extractors: [
    content => content.match(/[\w-/.:]+(?<!:)/g) || []
  ],
  content: []
}
