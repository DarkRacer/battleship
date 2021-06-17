import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BattleboardComponent} from './components/battleboard/battleboard.component';
import {BattleService} from './services/battle.service';
import {Battleboard2Component} from './components/battleboard2/battleboard2.component';
import { ScoreTableComponent } from './components/score-table/score-table.component';
import {LoginComponent} from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {CoreModule} from './core/core.module';
import { ConnectComponent } from './components/connect/connect.component';
import { HomeComponent } from './components/home/home.component';

@NgModule({
    declarations: [
        AppComponent,
        BattleboardComponent,
        Battleboard2Component,
        ScoreTableComponent,
        LoginComponent,
        ProfileComponent,
        ConnectComponent,
        HomeComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        FontAwesomeModule,
        CoreModule
    ],
    providers: [BattleService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
