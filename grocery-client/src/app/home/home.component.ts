import { Component, OnInit } from '@angular/core';
import {DataService} from "../services/data.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private data: DataService) { }

  items: Object;

  ngOnInit() {
    this.fetchItems();
  }

  private fetchItems() {
    this.data.getItems().subscribe(data => {
      this.items = data;
    })
  }

  delete(id: string) {
    this.data.deleteItem(id).subscribe(data => {
      this.fetchItems();
    });
  }

  getCategoryList(item: Object) {
    let catValues = "";
    if(item != null) {
      item['categories'].forEach(function(e) {
        catValues += " " +e.name;
      })
    }
    return catValues;
  }

}
