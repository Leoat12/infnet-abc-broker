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
          dataPoints: stockClose
        },
        {
          type: "line",
          dataPoints: ema
        }
      ]
    });

    this.chart.render();
  }

  changeEmaPeriod(period: number) {
    this.renderChart(period);
    this.currentPeriod = period;
  }
}
