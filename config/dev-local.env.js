"use strict";
const merge = require("webpack-merge");
const prodEnv = require("./prod.env");

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_URL: '"http://localhost:8000/api/"',
  PROXY_URL: '"http://localhost:8080/script_playground/api/"',
  USE_PROXY: "false",
  PORT: "8888"
});
