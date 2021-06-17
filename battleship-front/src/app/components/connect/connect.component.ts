import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-connect',
  templateUrl: './connect.component.html',
  styleUrls: ['./connect.component.scss']
})
export class ConnectComponent implements OnInit {
    redirectPath: string;
    codeGroup: FormGroup = new FormGroup({
        code: new FormControl('', [
            Validators.required
        ])
    });

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

    connection() {
      this.redirectPath = '/playWithFriend/' + this.codeGroup.controls.code.value.split('/')[0]
          + '/' + this.codeGroup.controls.code.value.split('/')[1];

      this.router.navigate([this.redirectPath]).then(() => {
            window.location.reload();
      });
    }
}
