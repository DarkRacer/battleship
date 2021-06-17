import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BattleService} from '../../services/battle.service';
import {ActivatedRoute} from '@angular/router';

import * as Stomp from 'stompjs';
import * as $ from 'jquery';
import {UserModel} from '../../model/user.model';

@Component({
    selector: 'app-battleboard',
    templateUrl: './battleboard.component.html',
    styleUrls: ['./battleboard.component.scss']
})

export class BattleboardComponent implements OnInit {
    constructor(private battleService: BattleService, private route: ActivatedRoute) {
    }
    initDone: boolean;
    showErrorMessage: boolean;
    userName: string;
    userId: string;
    socketUrl: string;
    boardLength;
    newBoardData;
    clickedCells = [''];
    currentMessage;
    user: UserModel;
    ws;
    opponentUserId;
    opponentUserName;
    shipCellCoordinates = [];
    ourTurn;
    gameover = false;
    playGameUrl;
    code: string;

    static highlightCells(values, className) {
        for (const item of values) {
            const elId2 = '#' + item.toString();
            $(elId2).addClass(className);
        }
    }
    ngOnInit() {
        this.userName = '';
        this.initDone = false;
        this.showErrorMessage = false;
        this.battleService.getInfo(localStorage.getItem('user_id')).subscribe( data => {
            this.user = data;
        });
        this.boardLength = Array.from({length: 10}, (v, k) => k);
    }

    onClick(event) {
        const value = (event.target || event.srcElement || event.currentTarget).attributes.id.nodeValue;
        if (this.ourTurn) {
            if (!this.clickedCells.includes(value)) {
                this.ourTurn = false;
                this.currentMessage = '';
                this.clickedCells.push(value);
                this.playAudioMiss();
                BattleboardComponent.highlightCells([value], 'attacked');
                this.sendName('po' + value);
            } else {
                this.currentMessage = 'Вы туда уже стреляли!';
            }
        }
    }

    initializeBoard(userId, callback1) {
        this.battleService.getNewBoard(userId).subscribe(
            data => {
                this.newBoardData = data;
            },
            err => {
                console.log(err);
            },
            () => {

                for (const i of this.newBoardData.shipPositions.FIRST_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.THIRD_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.FOURTH_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.THIRD_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_SUBMARINE) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship3');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_SUBMARINE) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship3');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_CRUISER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    BattleboardComponent.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship4');
                }
                this.userId = this.newBoardData.userId;

                console.log(JSON.stringify(this.newBoardData));
                this.userId = this.newBoardData.userId;
                this.socketUrl = this.newBoardData.socketUrl;
                this.code =  this.userId + '/' + this.socketUrl;
                callback1();
            });
    }
    connect() {
        const socket = new WebSocket('ws://localhost:8082/greeting');
        this.ws = Stomp.over(socket);
        const that = this;
        this.ws.connect({}, frame => {
            that.ws.subscribe('/errors', message => {
                alert('Error ' + message.body);
            });
            that.ws.subscribe('/topic/reply/' + this.socketUrl, message => {
                let tempInfo;
                let tempUserNameObject;
                if (message.body == 'start') {
                    setTimeout(() => {
                        this.battleService.getPlayer2Id(this.socketUrl).subscribe(
                            info => {
                                tempInfo = info;
                                this.opponentUserId = tempInfo.userId;
                            }, error1 => {
                                console.error(error1);
                            }, () => {
                                    this.battleService.getUserName(this.opponentUserId).subscribe(data => {
                                        tempUserNameObject = data;
                                    }, error => {
                                        console.error(error);
                                    }, () => {
                                        this.opponentUserName = tempUserNameObject.userName;
                                    });
                            });
                        }, 1500);

                } else {
                    const stringInfo = JSON.parse(message.body);
                    if (stringInfo.turnBy == 'p1') {
                        if (stringInfo.isContainsShip == 'true') {
                            this.ourTurn = false;
                            this.playAudioHit();
                            BattleboardComponent.highlightCells(['their' + stringInfo.attackedAt], 'attackedWithShip');
                        }
                    }
                    if (stringInfo.turnBy == 'p2') {
                        this.ourTurn = true;
                        if (stringInfo.isContainsShip == 'true') {
                            this.playAudioHit();
                            BattleboardComponent.highlightCells(['my' + stringInfo.attackedAt], 'attackedWithShip');
                        } else {
                            this.playAudioMiss();
                            BattleboardComponent.highlightCells(['my' + stringInfo.attackedAt], 'attacked');
                        }
                    }
                    if (stringInfo.winningMove == 'true') {
                        this.gameover = (stringInfo.winningMove == 'true');
                        console.log('Победный ход ' + stringInfo.winningMove);
                        this.ourTurn = false;
                        if (stringInfo.turnBy == 'p1') {
                            this.currentMessage = 'Победа.';
                        } else {
                            this.currentMessage = 'Проигрыш. ' + this.opponentUserName + ' победил.';
                        }

                    }
                }
            });
        }, error => {
            console.info('socket error');
        });
    }

    sendName(value) {
        const data = JSON.stringify({
            name: value
        });
        this.ws.send('/app/message/' + this.socketUrl, {}, data);
    }

    copyMessage(val: string) {
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = val;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy');
        document.body.removeChild(selBox);
    }

    newGame() {
        this.userName = this.user.userName;
        if (this.userName) {
            this.initDone = true;
            this.initializeBoard(this.user.userId, () => {
                this.connect();
            });
        } else {
            this.showErrorMessage = true;
        }
    }

    playAudioHit() {
        const audio = new Audio();
        audio.src = '../../../assets/sounds/hit.wav';
        audio.volume = 0.5;
        audio.load();
        audio.play();
    }

    playAudioMiss() {
        const audio = new Audio();
        audio.src = '../../../assets/sounds/miss.wav';
        audio.volume = 0.5;
        audio.load();
        audio.play();
    }
}
