/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.knockdata.spark.highcharts.plotoptions

import com.knockdata.spark.highcharts.model._
import com.knockdata.spark.highcharts.base._

private[highcharts] class SolidGauge extends BasePlotOptions with PublicApply {
  def fieldName = "solidgauge"


  def animation(value: Boolean): this.type = {
    append("animation", value)
  }

  def dataLabels(values: (String, Any)*): this.type = {
    append("dataLabels", values.toMap)
  }

  def linecap(value: String): this.type = {
    append("linecap", value)
  }

  def overshoot(value: Int): this.type = {
    append("overshoot", value)
  }

  def showCheckbox(value: Boolean): this.type = {
    append("showCheckbox", value)
  }

  def showInLegend(value: Boolean): this.type = {
    append("showInLegend", value)
  }

  def wrap(value: Boolean): this.type = {
    append("wrap", value)
  }
}
