/**
 * Copyright (c) 2015, CodiLime Inc.
 *
 * Created by: Grzegorz Swatowski
 */

'use strict';

exports.inject = function (module) {
  require('./status-bar/status-bar.js').inject(module);
  require('./graph-panel/graph-panel.module.js').inject(module);
};
