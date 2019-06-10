import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  currentUser: any;

  constructor(private router: Router, private authService: AuthService) {
    this.authService.currentUser.subscribe(x => this.currentUser = x);
  }

  appTitle: string = "ABC Grocery";

  ngOnInit() {
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
