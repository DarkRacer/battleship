import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BattleService} from '../../services/battle.service';
import {ActivatedRoute} from '@angular/router';

import * as Stomp from 'stompjs';
import * as $ from 'jquery';
import {UserModel} from '../../model/user.model';


@Component({
    selector: 'app-battleboard2',
    templateUrl: './battleboard2.component.html',
    styleUrls: ['./battleboard2.component.scss']
})

export class Battleboard2Component implements OnInit {

    constructor(private battleService: BattleService, private route: ActivatedRoute) {
    }
    initDone: boolean;
    showErrorMessage: boolean;
    initForm: FormGroup;
    userName: string;
    userId: string;
    socketUrl: string;
    user: UserModel;
    boardLength;
    newBoardData;
    clickedCells = [''];
    currentMessage;
    ws;
    opponentUserId;
    opponentUserName;
    shipCellCoordinates = [];
    ourTurn;
    gameover = false;

    static highlightCells(values, className) {
        for (const item of values) {
            const elId2 = '#' + item.toString();
            $(elId2).addClass(className);
        }
    }

    ngOnInit() {
        let tempUserNameObject;
        this.initDone = false;
        this.showErrorMessage = false;
        this.initForm = new FormGroup({
            name: new FormControl('', Validators.required)
        });
        this.boardLength = Array.from({length: 10}, (v, k) => k);
        this.opponentUserId = this.route.snapshot.params.userId;
        this.socketUrl = this.route.snapshot.params.socketUrl;
        this.battleService.getUserName(this.opponentUserId).subscribe(data => {
            tempUserNameObject = data;
        }, error => {
            console.error(error);
        }, () => {
            this.opponentUserName = tempUserNameObject.userName;
            console.log('Opp User Name: ' + this.opponentUserName);
        });
        this.battleService.getInfo(localStorage.getItem('user_id')).subscribe( data => {
            this.user = data;
            this.userName = this.user.userName;
            this.initializeBoard(this.user.id, this.socketUrl);
            this.connect();
        });
    }

    onClick(event) {
        const value = (event.target || event.srcElement || event.currentTarget).attributes.id.nodeValue;
        if (this.ourTurn) {
            if (!this.clickedCells.includes(value)) {
                this.ourTurn = false;
                this.currentMessage = '';
                this.clickedCells.push(value);
                this.playAudioMiss();
                Battleboard2Component.highlightCells([value], 'attacked');
                this.sendName('pt' + value);
            } else {
                this.currentMessage = 'Вы туда уже стреляли!';
            }
        }
    }

    initializeBoard(userId, socketUrl) {
        this.battleService.playWithFriend(userId, socketUrl).subscribe(
            data => {
                this.newBoardData = data;
                console.log('New Board Data: \n' + JSON.stringify(this.newBoardData));
            },
            err => {
                console.log(err);
            },
            () => {
                for (const i of this.newBoardData.shipPositions.FIRST_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.THIRD_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.FOURTH_ATTACKER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship1');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.THIRD_DESTROYER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship2');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_SUBMARINE) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship3');
                }
                for (const i of this.newBoardData.shipPositions.SECOND_SUBMARINE) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship3');
                }
                for (const i of this.newBoardData.shipPositions.FIRST_CRUISER) {
                    this.shipCellCoordinates.push((i < 10) ? ('my0' + i.toString()) : ('my' + i.toString()));
                    Battleboard2Component.highlightCells([this.shipCellCoordinates[this.shipCellCoordinates.length - 1]], 'ship4');
                }
                this.userId = this.newBoardData.userId;
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
                if (message.body == 'start') {
                    this.ourTurn = true;
                } else {
                    const stringInfo = JSON.parse(message.body);
                    console.log(stringInfo);
                    if (stringInfo.turnBy == 'p2') {
                        this.ourTurn = false;
                        if (stringInfo.isContainsShip == 'true') {
                            this.playAudioHit();
                            Battleboard2Component.highlightCells(['their' + stringInfo.attackedAt], 'attackedWithShip');
                        }
                    }
                    if (stringInfo.turnBy == 'p1') {
                        this.ourTurn = true;
                        if (stringInfo.isContainsShip == 'true') {
                            this.playAudioHit();
                            Battleboard2Component.highlightCells(['my' + stringInfo.attackedAt], 'attackedWithShip');
                        } else {
                            this.playAudioMiss();
                            Battleboard2Component.highlightCells(['my' + stringInfo.attackedAt], 'attacked');
                        }
                    }
                    if (stringInfo.winningMove == 'true') {
                        this.gameover = (stringInfo.winningMove == 'true');
                        console.log('Победный ход: ' + stringInfo.winningMove);
                        this.ourTurn = false;
                        if (stringInfo.turnBy == 'p2') {
                            this.currentMessage = 'Вы выиграли';
                        } else {
                            this.currentMessage = 'Вы проиграли ' + this.opponentUserName + ' победил';
                        }

                    }
                }
            });
            this.sendName('start');
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
