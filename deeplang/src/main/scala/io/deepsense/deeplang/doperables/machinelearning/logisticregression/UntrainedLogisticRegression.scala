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

package io.deepsense.deeplang.doperables.machinelearning.logisticregression

import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}

import io.deepsense.commons.types.ColumnType
import io.deepsense.commons.utils.DoubleUtils
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.reportlib.model.ReportContent

case class UntrainedLogisticRegression(
    modelParameters: LogisticRegressionParameters,
    createModel: () => LogisticRegressionWithLBFGS)
  extends LogisticRegression
  with Trainable {

  def this() = this(null, () => null)

  override def toInferrable: DOperable = new UntrainedLogisticRegression()

  override val train = new DMethod1To1[Trainable.Parameters, DataFrame, Scorable] {
    override def apply(
        context: ExecutionContext)(
        parameters: Trainable.Parameters)(
        dataFrame: DataFrame): Scorable = {

      val featureColumns = dataFrame.getColumnNames(parameters.featureColumns.get)
      val labelColumn = dataFrame.getColumnName(parameters.targetColumn.get)

      val labeledPoints = dataFrame.selectAsSparkLabeledPointRDD(
        labelColumn,
        featureColumns,
        labelPredicate = ColumnTypesPredicates.isNumericOrBinaryValued,
        featurePredicate = ColumnTypesPredicates.isNumeric)

      labeledPoints.cache()
      val trainedModel: LogisticRegressionModel = createModel().run(labeledPoints)
      val result = TrainedLogisticRegression(
        modelParameters,
        trainedModel,
        featureColumns,
        labelColumn)
      saveScorable(context, result)
      result
    }

    override def infer(
        context: InferContext)(
        parameters: Trainable.Parameters)(
        dataframeKnowledge: DKnowledge[DataFrame]): (DKnowledge[Scorable], InferenceWarnings) = {
      (DKnowledge(new TrainedLogisticRegression()), InferenceWarnings.empty)
    }
  }

  override def report(executionContext: ExecutionContext): Report = {
    DOperableReporter("Untrained Logistic Regression")
      .withParameters(
        description = "",
        ("Regularization", ColumnType.numeric,
          DoubleUtils.double2String(modelParameters.regularization)),
        ("Iterations number", ColumnType.numeric,
          DoubleUtils.double2String(modelParameters.iterationsNumber)),
        ("Tolerance", ColumnType.numeric,
          DoubleUtils.double2String(modelParameters.tolerance))
      )
      .report()
  }

  override def save(context: ExecutionContext)(path: String): Unit =
    throw new UnsupportedOperationException

}
