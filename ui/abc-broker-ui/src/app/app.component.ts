import {Component, OnInit} from '@angular/core';
import * as CanvasJS from "../assets/canvasjs.min.js";
import {Stock} from "./stock.model";
import {ApiService} from "./api.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'abc-broker-ui';
  data: Stock[] = []
  chart: CanvasJS.Chart;
  chart2: CanvasJS.Chart;

  readonly INITIAL_PERIOD: number = 9;
  currentPeriod = this.INITIAL_PERIOD;

  constructor(private api: ApiService) {
  }

  ngOnInit() {

    this.api.getStockInfo().subscribe(stocks => {
      this.data = stocks;
      this.renderChart(this.INITIAL_PERIOD);
    });
  }

  renderChart(period: number) {
    let stockClose = [];
    let ema = [];
    for (let stock of this.data) {
      let date = stock.date;
      stockClose.push({y: stock.close, label: date});
      if (stock.ema) {
        ema.push({y: stock.ema[period], label: date});
      }
    }
    this.chart = new CanvasJS.Chart("chartContainer", {
      zoomEnabled: true,
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Variação da Ação"
      },
      subtitles: [{
        text: "Tente dar zoom e e scroll vertical"
      }],
      data: [
        {
          type: "line",
          dataPoints: stockClose,
          showInLegend: true,
          legendText: 'Valor da Ação'
        },
        {
          type: "line",
          dataPoints: ema,
          showInLegend: true,
          legendText: 'EMA'
        }
      ]
    });

    this.chart.render();
    this.renderChart2();
  }

  renderChart2() {

    let macdLine = [];
    let histogram = [];
    let signalLine = [];

    for (let stock of this.data) {
      if (stock?.macd?.macd) {
        macdLine.push({y: stock.macd.macd, label: stock.macd.date})
      }

      if (stock?.macd?.histogram) {
        histogram.push({y: stock.macd.histogram, label: stock.macd.date});
      }

      if (stock?.macd?.signal) {
        signalLine.push({y: stock.macd.signal, label: stock.macd.date});
      }
    }

    this.chart2 = new CanvasJS.Chart("chartContainer2", {
      zoomEnabled: true,
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "MACD"
      },
      subtitles: [{
        text: "Tente dar zoom e e scroll vertical"
      }],
      data: [
        {
          type: "line",
          dataPoints: macdLine,
          showInLegend: true,
          legendText: "Linha MACD"
        },
        {
          type: "line",
          dataPoints: signalLine,
          showInLegend: true,
          legendText: "Linha de Sinal"
        },
        {
          type: "column",
          dataPoints: histogram,
        }
      ]
    });

    this.chart2.render();

  }

  changeEmaPeriod(period: number) {
    this.renderChart(period);
    this.currentPeriod = period;
  }
}
