'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_URL: '"/api/"',
  PROXY_URL: '"http://localhost:8080/script_playground/api/"',
  PORT: '8888'
})
