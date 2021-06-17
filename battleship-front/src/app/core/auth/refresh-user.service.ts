import { APP_INITIALIZER, Injectable, Provider } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { ExistingUser } from '../../model/existing-user.model';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap } from 'rxjs/operators';
import { CurrentUserService } from './current-user.service';

@Injectable({
  providedIn: 'root'
})
export class RefreshUserService {

  constructor(private http: HttpClient, private router: Router, private currentUserService: CurrentUserService) {}

  refreshCurrentUser(): Observable<void> {
    return this.http.get<ExistingUser | undefined>(`server/user/${localStorage.getItem('user_id')}`).pipe(
      catchError(() => of(undefined)),
      tap((profile) => {
        if (profile === undefined) {
          // @ts-ignore
          this.currentUserService.setUser(undefined);
        } else {
          // @ts-ignore
          this.currentUserService.setUser(profile);
        }
      })
    ) as Observable<void>;
  }
}

export function currentUserInitializerFactory(userService: RefreshUserService): () => Promise<void> {
  return () => {
    return userService.refreshCurrentUser().toPromise();
  };
}

export const CURRENT_USER_INITIALIZER: Provider = {
  provide: APP_INITIALIZER,
  useFactory: currentUserInitializerFactory,
  deps: [RefreshUserService],
  multi: true
};
