import {Component, OnInit} from '@angular/core';
import {BattleService} from '../../services/battle.service';
import {UserModel} from '../../model/user.model';

@Component({
    selector: 'app-score-table',
    templateUrl: './score-table.component.html',
    styleUrls: ['./score-table.component.scss']
})

export class ScoreTableComponent implements OnInit {

    constructor(private battleService: BattleService) {
    }

    users: UserModel[];

    ngOnInit() {
        this.battleService.getAllPlayerData().subscribe(data => {
            console.log(data);
            this.users = data;
        }, error => {
            console.error(error);
        }, () => {
        });


    }

    createUrl(userId: string) {
        return `http://localhost:4200/profile/${userId}`;
    }
}
