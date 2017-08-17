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

package com.knockdata.spark.highcharts.demo

import org.junit.Rule
import java.io.PrintWriter

import com.knockdata.spark.highcharts.model._
import com.knockdata.spark.highcharts._
import org.apache.spark.sql.functions._
import org.junit.Test
import org.junit.rules.TestName

// # Line Chart Demo
//
// Based on [Line Chart Demo](http://www.highcharts.com/demo/line-basic)
//
class DemoLineChart {
  val sqlContext = SparkEnv.sqlContext
  import sqlContext.implicits._
  val bank = DataSet.dfBank

  @Test
  def demoBasicLine(): Unit = {
    val Tokyo = Seq(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)
        .map(("Tokyo", _))
    val NewYork = Seq(-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)
      .map(("New York", _))
    val Berlin = Seq(-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)
      .map(("Berlin", _))
    val London = Seq(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8)
      .map(("London", _))

    val dataFrame = (Tokyo ++ NewYork ++ Berlin ++ London).toDF("city", "temperature")

    dataFrame.show()

    val chart = highcharts(dataFrame
      .seriesCol("city")
      .series("y" -> col("temperature")))

    chart.plot()

    new PrintWriter(s"target/demoBasicLine.json") { write(chart.replaced); close }
  }


  // ## Line Chart Basic
  //
  // Based on [Highcharts Demo Line Basic](http://www.highcharts.com/demo/line-basic)
  //
  // an line chart with
  //
  // * x axis data from column $"age"
  // * y axis aggregated the average balance
  // * data point order by age
  //
  @Test
  def demoLineBasic(): Unit = {
    val chart = highcharts(bank
      .series("x" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age")))

    chart.plot()

    new PrintWriter(s"target/demoLineBasic.json") { write(chart.replaced); close }

  }

  // ## Line Chart Basic, Explicitly Ascending Order
  //
  // Based on [Highcharts Demo Line Basic](http://www.highcharts.com/demo/line-basic)
  //
  // an line chart with
  //
  // * x axis data from column $"age"
  // * y axis aggregated the average balance
  // * data point order by age, specified EXPLICITLY ascending order
  //
  @Test
  def demoLineBasicAsc(): Unit = {

    val chart = highcharts(bank
      .series("x" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age").asc))

    chart.plot()

    new PrintWriter("target/demoLineBasicAsc.json") { write(chart.replaced); close }

  }

  // ## Line Chart Basic, Descending Order
  //
  // Based on [Highcharts Demo Line Basic](http://www.highcharts.com/demo/line-basic)
  //
  // an line chart with
  //
  // * x axis data from column $"age"
  // * y axis aggregated the average balance
  // * data point order by age, descending order
  //
  @Test
  def demoLineBasicDesc(): Unit = {

    val chart = highcharts(bank
      .series("name" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age").desc))
      .xAxis(new XAxis("age").typ("category"))


    chart.plot()

    new PrintWriter("target/demoLineBasicDesc.json") { write(chart.replaced); close }

  }

  // ## Line Chart Multiple Series
  //
  // Based on [Highcharts Demo Line Basic](http://www.highcharts.com/demo/line-basic)
  //
  // an line chart with
  //
  // * create multiple series according to $"marital" column
  // * x axis data from column $"age"
  // * y axis aggregated the average balance
  // * data point order by age, descending order
  //
  @Test
  def demoLineBasicMultipleSeries(): Unit = {
    val chart = highcharts(bank.seriesCol("marital")
      .series("name" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age")))

    chart.plot()

    new PrintWriter("target/demoLineBasicMultipleSeries.json") { write(chart.replaced); close }
  }

  // ## Line Chart Multiple Series, With Options
  //
  // Based on [Highcharts Demo Line Basic](http://www.highcharts.com/demo/line-basic)
  //
  // an line chart with
  //
  // * create multiple series according to $"marital" column
  // * x axis data from column $"age"
  // * y axis aggregated the average balance
  // * data point order by age
  //
  @Test
  def demoLineBasicMultipleSeriesWithOption(): Unit = {
    val chart = highcharts(bank.seriesCol("marital")
      .series("name" -> "age",
        "y" -> avg(col("balance")))
      .orderBy(col("age")))
      .title(new Title("Marital Job Average Balance").x(-20))
      .subtitle(new Subtitle("Source: Zeppelin Tutorial").x(-20))
      .xAxis(new XAxis("Age").typ("category"))
      .yAxis(new YAxis("Balance(¥)").plotLines(
        Map("value" -> 0, "width" -> 1, "color" -> "#808080")))
      .tooltip(new Tooltip().valueSuffix("¥"))
      .legend(new Legend().layout("vertical").align("right")
        .verticalAlign("middle").borderWidth(0))
      .plotOptions(PlotOptions.series.threshold(3000))

    chart.plot()

    new PrintWriter("target/demoLineBasicMultipleSeriesWithOption.json") { write(chart.replaced); close }

  }

  // ## Line Chart, With Data Labels
  //
  // Based on [Highchart Line Charts Demo With data labels](http://www.highcharts.com/demo/line-labels)
  //
  // an line chart with
  //
  // * name data(xAxis) from column $"name"
  // * y axis aggregated the average balance
  // * data point order by $"job"
  //
  @Test
  def demoLineWithDataLabels(): Unit = {
    val chart = highcharts(bank.series("name" -> "job", "y" -> avg(col("balance")))
      .orderBy(col("job")))
      .plotOptions(PlotOptions.line
        .dataLabels("enabled" -> true, "format" -> "{point.y:.2f}"))
      .tooltip(new Tooltip().valueDecimals(2))

    chart.plot()

    new PrintWriter("target/demoLineWithDataLabels.json") { write(chart.replaced); close }

  }

  // ## Line Chart Zoomable
  //
  // Based on [Highchart Line Charts Zoomable](http://www.highcharts.com/demo/line-time-series)
  //
  // an line chart with
  //
  // * name data(xAxis) from column $"age"
  // * y axis aggregated the average balance
  // * data point order by $"job"
  //
  //
  // NOTE:
  // linearGradient is not described in [Highcharts API](http://api.highcharts.com/highcharts#plotOptions.area.fillColor)
  //
  @Test
  def demoLineZoomable(): Unit = {

    val options = PlotOptions.area
      .fillColorLinearGradient("x1" -> 0, "y1" -> 0, "x2" -> 0, "y2" -> 1)
      .fillColorStops((0, "Highcharts.getOptions().colors[0]"),
          (1, "Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')"))


    val chart = highcharts(bank.series("name" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age")))
      .chart(Chart.area.zoomType("x"))
      .plotOptions(options)

    chart.plot()

    new PrintWriter("target/demoLineZoomable.json") { write(chart.replaced); close }
  }

  // ## Spline Inverted
  //
  // Based on [Highchart Spline Line Inverted](http://www.highcharts.com/demo/spline-inverted)
  //
  // an line chart with
  //
  // * name data(xAxis) from column $"age"
  // * y axis aggregated the average balance
  // * data point order by $"job"
  //
  @Test
  def demoSplineInverted(): Unit = {
    val chart = highcharts(bank.series("x" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age")))
      .chart(Chart.spline.inverted(true))

    chart.plot()
    new PrintWriter("target/demoSplineInverted.json") { write(chart.replaced); close }
  }

//  @Test
//  def demoSplineWithSymbols(): Unit = {
//    // TODO
//  }

  // ## Spline With Plot Bands
  //
  // Based on [Spline Plot Bands](http://www.highcharts.com/demo/spline-plot-bands)
  //
  // an line chart with
  //
  // * name data(xAxis) from column $"age"
  // * y axis aggregated the average balance
  // * data point order by $"job"
  //
  @Test
  def demoSplineWithPlotBands(): Unit = {
    val yAxis = new YAxis("Average Balance").plotBands(
      Map("from" -> 0, "to" -> 1000, "color" -> "rgba(68, 170, 213, 0.1)",
        "label" -> Map(
          "text" -> "Low",
          "style" -> Map(
            "color" -> "#606060"
          )
        )
      ),
      Map("from" -> 5000, "to" -> 10000, "color" -> "rgba(68, 170, 213, 0.1)",
        "label" -> Map(
          "text" -> "High",
          "style" -> Map(
            "color" -> "#606060"
          )
        )
      )
    )

    val chart = highcharts(bank.series("x" -> "age", "y" -> avg(col("balance")))
      .orderBy(col("age")))
      .yAxis(yAxis)

    chart.plot()
    new PrintWriter("target/demoSplineWithPlotBands.json") { write(chart.replaced); close }
  }

  // ## Time Data With Irregular Intervals
  //
  // Based on [Time Data With Irregular Intervals](http://www.highcharts.com/demo/spline-irregular-time)
  //
  // an line chart with
  //
  // * multiple series using column $"year"
  // * x axis from $"time"
  // * y axis using $"depth"
  //
  @Test
  def demoTimeDataWithIrregularIntervals(): Unit = {
    val chart = highcharts(DataSet.dfSnowDepth.seriesCol("year")
      .series("x" -> "time", "y" -> "depth"))
      .chart(Chart.spline)
      .title(new Title("Snow depth at Vikjafjellet, Norway"))
      .subtitle(new Subtitle("Irregular time data in Highcharts JS"))
      .xAxis(new XAxis("Date").typ("datetime").dateTimeLabelFormats(
        "month" -> "%e. %b", "year" -> "%b"))
      .yAxis(new YAxis("Snow depth (m)").min(0))
      .tooltip(new Tooltip().headerFormat("<b>{series.name}</b><br>").pointFormat(
        "{point.x:%e. %b}: {point.y:.2f} m"))
      .plotOptions(PlotOptions.spline.marker("enabled" -> true))

    chart.plot()
    new PrintWriter("target/demoTimeDataWithIrregularIntervals.json") { write(chart.replaced); close }
  }
}
