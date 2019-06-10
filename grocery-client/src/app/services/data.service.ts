import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  getItem(id: string) {
    return this.http.get(`${environment.baseUrl}/items/${id}`);
  }

  getItems() {
    return this.http.get(`${environment.baseUrl}/items`);
  }

  getCategories() {
    return this.http.get(`${environment.baseUrl}/categories`);
  }

  saveItem(item: Object) {
    return this.http.post(`${environment.baseUrl}/admin/items`, item);
  }

  updateItem(id:string, item: object) {
    return this.http.post(`${environment.baseUrl}/admin/items/${id}`, item);
  }

  deleteItem(id: string) {
    return this.http.delete(`${environment.baseUrl}/admin/items/${id}`);
  }

}
