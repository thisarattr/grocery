import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-add-item',
  templateUrl: './add-item.component.html',
  styleUrls: ['./add-item.component.scss']
})
export class AddItemComponent implements OnInit {

  createForm: FormGroup;
  submitted = false;
  success = false;
  categories: Object;
  savedItem: Object;

  constructor(private formBuilder: FormBuilder, private data: DataService) { }

  ngOnInit() {
    this.data.getCategories().subscribe(data => {
      this.categories = data['categories'];
    })

    this.createForm = this.formBuilder.group({
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
    this.submitted = true;

    if (this.createForm.invalid) {
      return;
    }

    console.log(this.createForm.getRawValue());
    console.log(this.createForm.value);

    this.data.saveItem(this.createForm.value).subscribe(data => {
      this.savedItem = data;
      this.createForm.reset();
    })

    this.success = (this.savedItem != null);
  }

}

