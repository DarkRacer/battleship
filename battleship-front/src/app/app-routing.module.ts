import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {BattleboardComponent} from './components/battleboard/battleboard.component';
import {Battleboard2Component} from './components/battleboard2/battleboard2.component';
import {ScoreTableComponent} from './components/score-table/score-table.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuardGuard} from './core/guard/auth-guard.guard';
import {ProfileComponent} from './components/profile/profile.component';
import {ConnectComponent} from "./components/connect/connect.component";
import {HomeComponent} from "./components/home/home.component";

const routes: Routes = [{
    path: 'battleboard',
    component: BattleboardComponent,
    canActivate: [AuthGuardGuard]
}, {
    path: 'playWithFriend/:userId/:socketUrl',
    component: Battleboard2Component,
    canActivate: [AuthGuardGuard]
}, {
    path: 'scoreTable',
    component: ScoreTableComponent,
    canActivate: [AuthGuardGuard]
}, {
    path: 'login',
    component: LoginComponent
}, {
    path: 'profile/:userId',
    component: ProfileComponent,
    canActivate: [AuthGuardGuard]
}, {
    path: 'connect',
    component: ConnectComponent,
    canActivate: [AuthGuardGuard]
}, {
    path: '',
    component: HomeComponent
}];

@NgModule({
    imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
