/**
 * Copyright 2015, deepsense.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler, StandardScalerModel => SparkStandardScalerModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.spark.wrappers.models.StandardScalerModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.doperables.{Report, SparkEstimatorWrapper}
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class StandardScaler
  extends SparkEstimatorWrapper[SparkStandardScalerModel, SparkStandardScaler, StandardScalerModel]
  with HasInputColumn
  with HasOutputColumn {

  val withMean = new BooleanParamWrapper[SparkStandardScaler](
    name = "with mean",
    description = "Centers the data with mean before scaling",
    sparkParamGetter = _.withMean)
  setDefault(withMean, false)

  val withStd = new BooleanParamWrapper[SparkStandardScaler](
    name = "with std",
    description = "Scales the data to unit standard deviation",
    sparkParamGetter = _.withStd)
  setDefault(withStd, true)

  override def report(executionContext: ExecutionContext): Report = Report()

  override val params: Array[Param[_]] = declareParams(withMean, withStd, inputColumn, outputColumn)
}
