import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {Stock} from "./stock.model";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) {
  }

  getStockInfo(): Observable<Stock[]> {
    return this.http.get<Stock[]>("http://localhost:8080/api/stock");
  }

}
