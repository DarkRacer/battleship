import {  Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpBackend, HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Role } from './role.model';
import { Anonymous, CurrentUser, LoggedUser } from './current-user.model';

interface ApiProfile {
  username: string;
  roles: [Role];
}

export class AnonymousUserImpl implements Anonymous {
  readonly authenticated: false = false;

  hasRole(role: Role): boolean {
    return false;
  }
}

export class CurrentUserImpl implements LoggedUser {
  readonly authenticated: true = true;
  readonly username = this.profile.username;

  private roles: Set<Role> = new Set(this.profile.roles);

  constructor(readonly profile: ApiProfile) {}

  hasRole(role: Role): boolean {
    return this.roles.has(role);
  }
}

@Injectable({
  providedIn: `root`
})
export class CurrentUserService {
  user$: BehaviorSubject<CurrentUser> = new BehaviorSubject<CurrentUser>(new AnonymousUserImpl());
  private http: HttpClient;

  constructor(private httpClient: HttpBackend, private router: Router, private http1: HttpClient) {
    this.http = new HttpClient(httpClient);
  }

   login(provider: string) {

    return this.http.post(`service/oauth2/authorization/vk?redirect_uri=http://localhost:8082/user`,
      '' , { responseType: 'text' });

  }

  o(data: any) {
    data.json();
  }

  setUser(user: CurrentUser): void {
    this.user$.next(user);
  }

  getAuth(code: string, uuid: string | undefined): Observable<any> {
    const body = { code, uuid };
    return this.http.post(`server/auth/code`, body, {headers: new HttpHeaders({
        'Content-Type': 'application/json'})});
  }
}
