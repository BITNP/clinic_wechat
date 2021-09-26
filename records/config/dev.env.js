'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_URL: '"/api/"',
  PROXY_URL: '"https://clinicp.bitnp.net/proxy"',
  USER_URL: '"https://clinicp.bitnp.net/user"',
  PORT: '8888'
})
