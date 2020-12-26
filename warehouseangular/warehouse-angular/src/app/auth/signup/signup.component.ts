import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SignupRequestPayload } from './singup-request.payload';
import { AuthService } from '../service/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import {RouteService} from '../service/route-service.service';
import {Depot} from '../model/depot';
import {Route} from '../model/route';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: {  username: string;
    password: string;
    email: string;
    role: string;
    firstName: string;
    lastName: string;
    depot_id: any};
  signupForm: FormGroup;
  depot: Depot[];
  route: Route[];

  constructor(private authService: AuthService, private router: Router,
              private toastr: ToastrService, private routeService: RouteService) {
    this.signupRequestPayload = {
      username: '',
      email: '',
      password: '',
      role: '',
      firstName: '',
      lastName: '',
      depot_id: ''
    };
  }

  // tslint:disable-next-line:typedef
  ngOnInit() {
    this.routeService.findAllDepots().subscribe((data) => this.depot = data);
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      role: new FormControl('', Validators.required),
      depot_id: new FormControl('', Validators.required)
    });
  }

  // tslint:disable-next-line:typedef
  signup() {
    this.signupRequestPayload.email = this.signupForm.get('email').value;
    this.signupRequestPayload.username = this.signupForm.get('username').value;
    this.signupRequestPayload.password = this.signupForm.get('password').value;
    this.signupRequestPayload.firstName = this.signupForm.get('firstName').value;
    this.signupRequestPayload.lastName = this.signupForm.get('lastName').value;
    this.signupRequestPayload.depot_id = this.signupForm.get('depot_id').value;
    this.authService.signup(this.signupRequestPayload)
      .subscribe(() => {
        this.router.navigate(['/login'],
          { queryParams: { registered: 'true' } });
      }, error => {
        console.log(error);
        this.toastr.error('Registration Failed! Please try again');
      });
  }
}
