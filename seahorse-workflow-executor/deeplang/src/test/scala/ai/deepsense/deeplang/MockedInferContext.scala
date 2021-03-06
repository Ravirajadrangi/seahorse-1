/**
 * Copyright 2016 deepsense.ai (CodiLime, Inc)
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

package ai.deepsense.deeplang

import org.scalatest.mockito.MockitoSugar

import ai.deepsense.commons.rest.client.datasources.{DatasourceClient, DatasourceInMemoryClientFactory}
import ai.deepsense.deeplang.catalogs.CatalogPair
import ai.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import ai.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import ai.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import ai.deepsense.deeplang.inference.InferContext

object MockedInferContext extends MockitoSugar {

  def apply(
      dOperableCatalog: DOperableCatalog =
      CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperableCatalog,
      dataFrameBuilder: DataFrameBuilder = mock[DataFrameBuilder],
      dOperationsCatalog: DOperationsCatalog = mock[DOperationsCatalog],
      datasourceClient: DatasourceClient = new DatasourceInMemoryClientFactory(List.empty).createClient
  ): InferContext = {
    val catalogPair = CatalogPair(dOperableCatalog, dOperationsCatalog)
    InferContext(
      dataFrameBuilder,
      catalogPair,
      datasourceClient
    )
  }

}
