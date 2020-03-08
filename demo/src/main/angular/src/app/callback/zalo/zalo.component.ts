import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@shared/service';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-zalo',
  templateUrl: './zalo.component.html',
  styleUrls: ['./zalo.component.scss']
})
export class ZaloComponent implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {
    this.authenticationSuccessHandler();
  }

  ngOnInit() {}

  private authenticationSuccessHandler() {
    this.route.queryParams
      .pipe(
        map(queryParams => queryParams['exchange_token']),
        switchMap(exchangeToken =>
          this.authService.exchangeToken(exchangeToken)
        )
      )
      .subscribe(
        token => {
          this.authService.saveToken(token);
          this.router.navigate(['/']);
        },
        error => {
          this.router.navigate(['authx']);
        }
      );
  }
}
