import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BattleService} from '../../services/battle.service';
import {UserModel} from '../../model/user.model';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
    change = false;
    user: UserModel;
    proc: number;
    name: string;
    status: string;
    userGroup: FormGroup = new FormGroup({
        userName: new FormControl('', [
            Validators.required,
            Validators.pattern('^[a-zA-Zа-яА-Я]*')
        ]),
        status: new FormControl('', [
            Validators.required,
        ])
    });

  constructor(private route: ActivatedRoute, private battleService: BattleService) { }

  ngOnInit(): void {
      if (this.route.snapshot.params.userId === localStorage.getItem('user_id')) {
          this.change = true;
      }
      this.battleService.getInfo(this.route.snapshot.params.userId).subscribe(data => {
          this.user = data;
          this.proc = (this.user.win / (this.user.win + this.user.lost)) * 100;
          this.userGroup.controls.userName.setValue(this.user.userName);
          this.userGroup.controls.status.setValue(this.user.status);
          console.log(this.user);
      });
  }

    save() {
        this.user.userName = this.userGroup.controls.userName.value;
        this.user.status = this.userGroup.controls.status.value;

        this.battleService.saveUser(this.user).subscribe();
    }
}
