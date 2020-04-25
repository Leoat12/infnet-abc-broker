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
      // test
      // console.log(this.data);
    });

    let dataPoints = [];
    let y = 0;
    for (var i = 0; i < 10000; i++) {
      y += Math.round(5 + Math.random() * (-5 - 5));
      dataPoints.push({y: y});
    }
    let chart = new CanvasJS.Chart("chartContainer", {
      zoomEnabled: true,
      animationEnabled: true,
      exportEnabled: true,
      title: {
        text: "Performance Demo - 10000 DataPoints"
      },
      subtitles: [{
        text: "Try Zooming and Panning"
      }],
      data: [
        {
          type: "line",
          dataPoints: dataPoints
        }]
    });

    chart.render();
  }

}
