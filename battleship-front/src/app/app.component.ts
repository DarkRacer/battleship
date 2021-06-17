import {Component, OnInit} from '@angular/core';
import {BattleboardComponent} from './components/battleboard/battleboard.component';
import {ExistingUser} from './model/existing-user.model';
import {AnonymousUserImpl, CurrentUserService} from './core/auth/current-user.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
    title = 'battleship-front';
    user$ = this.currentUserService.user$;
    user: ExistingUser;
    isLoggedIn = false;
    show = false;

    constructor(
        private readonly currentUserService: CurrentUserService
    ) {}

    ngOnInit(): void {
        this.isLoggedIn = !!localStorage.getItem('auth_token');

        if (this.isLoggedIn) {
            // @ts-ignore
            this.user = this.user$.getValue();
            console.log(this.user);
        }
    }
    logout() {
        this.currentUserService.user$.next(new AnonymousUserImpl());
        localStorage.clear();
        window.location.reload();
    }
}
