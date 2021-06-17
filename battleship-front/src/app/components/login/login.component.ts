import {Component, OnInit} from '@angular/core';
import {CurrentUserService} from '../../core/auth/current-user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshUserService } from '../../core/auth/refresh-user.service';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  content: string | undefined;
  url: string | undefined;
  redirectPath: string | undefined;

  constructor(
    private readonly currentUserService: CurrentUserService,
    private router: Router,
    private cookie: CookieService,
    private readonly refreshUserService: RefreshUserService,
  ) { }

  ngOnInit(): void {
      if (this.cookie.check('Authorization') && this.cookie.check('user_id')) {
          localStorage.setItem('auth_token', this.cookie.get('Authorization'));
          localStorage.setItem('user_id', this.cookie.get('user_id'));
          this.redirectPath = '/profile/' + this.cookie.get('user_id');
          this.cookie.deleteAll();
          this.refreshUserService.refreshCurrentUser().subscribe();
          this.router.navigate([this.redirectPath]).then(() => {
              window.location.reload();
          });
    } else {

      this.url = 'http://localhost:8082/oauth2/authorization/vk?' +
        'redirect_uri=http://localhost:4200/login';
    }
  }

  login(provider: string): void {
    this.currentUserService
      .login(provider)
      .subscribe(
        (response: any) => {
            this.content = response;
        },
        (error) => {
          console.error('Error', error);
        }
      );
  }
}
