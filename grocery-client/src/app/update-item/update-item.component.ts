import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DataService } from "../services/data.service";

@Component({
  selector: 'app-update-item',
  templateUrl: './update-item.component.html',
  styleUrls: ['./update-item.component.scss']
})
export class UpdateItemComponent implements OnInit {

  id: string;
  updateForm: FormGroup;
  submitted = false;
  success = false;
  categories: Object;
  retrievedItem: Object;

  constructor(private route: ActivatedRoute, private formBuilder: FormBuilder, private data: DataService) {
    this.route.params.subscribe( data => {
      this.id = data.id;
      console.log(this.id);
    });
  }

  ngOnInit() {

    this.data.getCategories().subscribe(data => {
      this.categories = data['categories'];
    })

    this.data.getItem(this.id).subscribe(data => {
      this.retrievedItem = data;
      console.log(data);

    })

    this.updateForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      categories: ['', Validators.required],
      sku: ['', Validators.required],
      dimensions: [''],
      madeIn: [''],
      price: ['', Validators.required]

    });

  }

  onSubmit() {
    console.log(this.retrievedItem);
    this.data.updateItem(this.id, this.retrievedItem).subscribe(data => {
      this.submitted = true;
      this.success = true;
    });
  }

}
