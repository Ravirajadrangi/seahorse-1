/**
 * Copyright 2016, deepsense.io
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

package io.deepsense.commons.rest.client.datasources

import java.util.UUID

import io.deepsense.api.datasourcemanager.model.Datasource
import io.deepsense.commons.rest.client.datasources.DatasourceTypes.{DatasourceId, DatasourceList, DatasourceMap}

class DatasourceInMemoryClient(datasourceList: DatasourceList) extends DatasourceClient {
  val datasourceMap = toMap(datasourceList)

  def toMap(datasourceList: DatasourceList): DatasourceMap = {
    datasourceList.foldLeft(Map.empty[DatasourceId, Datasource])( (dsMap, ds) =>
      dsMap + (ds.getId() -> ds)
    )
   }

  def toFactory: DatasourceClientFactory = {
    new DatasourceInMemoryClientFactory(datasourceList)
  }
  def getDatasource(uuid: UUID): Option[Datasource] = {
    datasourceMap.get(uuid.toString)
  }
}

class DatasourceInMemoryClientFactory(datasourceMap: DatasourceList)
  extends DatasourceClientFactory {

  override def createClient: DatasourceClient = {
    new DatasourceInMemoryClient(datasourceMap)
  }
}
