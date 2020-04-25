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

  constructor(private api: ApiService) {
  }

  ngOnInit() {

    this.api.getStockInfo().subscribe(stocks => {
      this.data = stocks;
      let dataPoints = [];
      let y = 0;
      for (let stock of this.data) {
        let date = stock.date.toString();
        dataPoints.push({y: stock.close, label: date});
      }
      let chart = new CanvasJS.Chart("chartContainer", {
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
            dataPoints: dataPoints
          }]
      });

      chart.render();
    });
  }

}
