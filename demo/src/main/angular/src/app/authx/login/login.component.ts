import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: FormGroup;

  constructor() {}

  ngOnInit() {}

  onSubmit() {
    this.loginByZalo();
  }

  loginByZalo() {
    const targetUrl = `${environment.serverUrl}/oauth2/authorization/zalo`;
    const redirectUrl = new URL(targetUrl, window.location.origin);
    window.location.href = redirectUrl.toString();
  }
}
