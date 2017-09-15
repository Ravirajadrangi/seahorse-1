/**
 * Copyright (c) 2015, CodiLime Inc.
 */
'use strict';

var angular = require('angular');

/**
 * Reports module.
 */
var reports = angular.module('ds.reports', []);

require('./reports.controller.js').inject(reports);
require('./reports.config.js').inject(reports);

require('./report-side-panel/report-side-panel.js').inject(reports);

require('./report-dataframe/report-dataframe.js').inject(reports);
require('./report-cross-validate-regressor/report-cross-validate-regressor.js').inject(reports);
require('./report-evaluate-scoring/report-evaluate-scoring.js').inject(reports);

require('./report-table/report-table-header/report-table-header.js').inject(reports);
require('./report-table/report-table-body/report-table-body.js').inject(reports);

require('./charts/distribution-categorical-chart.js').inject(reports);
require('./charts/distribution-continuous-chart.js').inject(reports);

require('./charts/box-plot.js').inject(reports);
require('./charts/column-plot.js').inject(reports);
require('./charts/pie-plot.js').inject(reports);

module.exports = reports;
