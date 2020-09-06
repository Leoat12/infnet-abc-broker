
interface Macd {
  date : Date;
  macd : number;
  histogram : number;
}
export interface Stock {
  date: Date;
  open: number;
  high: number;
  low: number;
  close: number;
  adjClose: number;
  volume: number;
  sma: { [period: number]: number };
  ema: { [period: number]: number };
  macd : Macd;
}