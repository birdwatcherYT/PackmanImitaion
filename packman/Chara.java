package packman;

import static packman.Packman.*;
import packman.Packman.shift;

public class Chara {

    public int x, y;
    public static int stagePanel[][];
    public boolean left, right, up, down, back;
    public int tempX, tempY;
    EnemyAI ai;

    Chara() {
        back = false;
        flagset(false, false, false, false);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //ステージ上での押して良いキー、悪いキーのセット(stagePanel[i][j]の下6ビットは[power][dot][up][down][left][right]の順にフラグがある)
    static void stageSet() {
        stagePanel = new int[22][19];
        for (int i = 0; i < 19; i++) {
            if (i == 5 || i == 13) {
                stagePanel[0][i] = 7;
                stagePanel[21][i] = 11;
            } else if (i == 0) {
                stagePanel[0][i] = 5;
                stagePanel[21][i] = 9;
            } else if (i == 18) {
                stagePanel[0][i] = 6;
                stagePanel[21][i] = 10;
            } else {
                stagePanel[0][i] = 3;
                stagePanel[21][i] = 3;
            }
            if (i == 0 || i == 5 || i == 13 || i == 18) {
                stagePanel[1][i] = 12;
                stagePanel[2][i] = 12;
                stagePanel[4][i] = 12;
                stagePanel[18][i] = 12;
                stagePanel[19][i] = 12;
                stagePanel[20][i] = 12;
            } else {
                stagePanel[1][i] = 0;
                stagePanel[2][i] = 0;
                stagePanel[4][i] = 0;
                stagePanel[18][i] = 0;
                stagePanel[19][i] = 0;
                stagePanel[20][i] = 0;
            }
            if (i == 0 || i == 18) {
                stagePanel[3][i] = 12;
            } else if (6 <= i && i <= 12) {
                stagePanel[3][i] = 3;
            } else if (i == 5) {
                stagePanel[3][i] = 13;
            } else if (i == 13) {
                stagePanel[3][i] = 14;
            } else {
                stagePanel[3][i] = 0;
            }
            if (i == 0) {
                stagePanel[5][i] = 9;
                stagePanel[17][i] = 5;
            } else if (i == 2 || i == 4 || i == 14 || i == 16) {
                stagePanel[5][i] = 7;
                stagePanel[17][i] = 11;
            } else if (i == 5 || i == 13) {
                stagePanel[5][i] = 11;
                stagePanel[17][i] = 7;
            } else if (i == 18) {
                stagePanel[5][i] = 10;
                stagePanel[17][i] = 6;
            } else if (i == 9) {
                stagePanel[5][i] = 0;
                stagePanel[17][i] = 3;
            } else if (i == 8) {
                stagePanel[5][i] = 6;
                stagePanel[17][i] = 3;
            } else if (i == 10) {
                stagePanel[5][i] = 5;
                stagePanel[17][i] = 3;
            } else {
                stagePanel[5][i] = 3;
                stagePanel[17][i] = 3;
            }
            if (i == 2 || i == 4 || i == 8 || i == 10 || i == 14 || i == 16) {
                stagePanel[6][i] = 12;
            } else {
                stagePanel[6][i] = 0;
            }
            if (i == 2 || i == 4 || i == 14 || i == 16) {
                stagePanel[7][i] = 12;
            } else if (i == 6) {
                stagePanel[7][i] = 5;
            } else if (i == 12) {
                stagePanel[7][i] = 6;
            } else if (i == 8 || i == 10) {
                stagePanel[7][i] = 11;
            } else if (i == 9 || i == 7 || i == 11) {
                stagePanel[7][i] = 3;
            } else {
                stagePanel[7][i] = 0;
            }
            if (i == 2 || i == 4 || i == 6 || i == 12 || i == 14 || i == 16) {
                stagePanel[8][i] = 12;
                stagePanel[9][i] = 12;
                stagePanel[10][i] = 12;
                stagePanel[12][i] = 12;
            } else if (i == 9) {
                stagePanel[8][i] = 8;
                stagePanel[9][i] = 8;
                stagePanel[10][i] = 8;
            } else {
                stagePanel[8][i] = 0;
                stagePanel[9][i] = 0;
                stagePanel[10][i] = 0;
                stagePanel[12][i] = 0;
            }
            if (7 <= i && i <= 11) {
                stagePanel[11][i] = 0;
            } else if (i == 2 || i == 4 || i == 14 || i == 16) {
                stagePanel[11][i] = 15;
            } else if (i == 6) {
                stagePanel[11][i] = 14;
            } else if (i == 12) {
                stagePanel[11][i] = 13;
            } else {
                stagePanel[11][i] = 3;
            }
            if (i == 2 || i == 4 || i == 14 || i == 16) {
                stagePanel[13][i] = 12;
            } else if (i == 6) {
                stagePanel[13][i] = 9;
            } else if (i == 12) {
                stagePanel[13][i] = 10;
            } else if (7 <= i && i <= 11) {
                stagePanel[13][i] = 3;
            } else {
                stagePanel[13][i] = 0;
            }
            if (i == 2 || i == 4 || i == 14 || i == 16) {
                stagePanel[14][i] = 12;
                stagePanel[16][i] = 12;
            } else {
                stagePanel[14][i] = 0;
                stagePanel[16][i] = 0;
            }
            if (i == 2 || i == 16) {
                stagePanel[15][i] = 12;
            } else if (i == 4) {
                stagePanel[15][i] = 13;
            } else if (i == 14) {
                stagePanel[15][i] = 14;
            } else if (5 <= i && i <= 13) {
                stagePanel[15][i] = 3;
            } else {
                stagePanel[15][i] = 0;
            }
        }
        //エサのフラグ
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 19; j++) {
                if (stagePanel[i][j] != 0 && !(j == 9 && 8 <= i && i <= 10)) {
                    stagePanel[i][j] |= 1 << shift.DOT.ordinal();
                }
            }
        }
        //パワーエサのフラグ
        stagePanel[0][0] |= 1 << shift.POWER.ordinal();
        stagePanel[0][18] |= 1 << shift.POWER.ordinal();
        stagePanel[21][0] |= 1 << shift.POWER.ordinal();
        stagePanel[21][18] |= 1 << shift.POWER.ordinal();
    }

    void flagset(boolean u, boolean d, boolean l, boolean r) {
        up = u;
        down = d;
        right = r;
        left = l;
    }

    void stage() {
        //ステージの縁とワープの実装
        if (x < 0 && !((y + PANELSIZE / 2 > 330) && (y + PANELSIZE / 2 < 360))) {
            x = 0;
        } else if (x + PANELSIZE > packman.Packman.PAINT_W && !((y + PANELSIZE / 2 > 330) && (y + PANELSIZE / 2 < 360))) {
            x = packman.Packman.PAINT_W - PANELSIZE;
        } else if (x + PANELSIZE / 2 < 0 && (y + PANELSIZE / 2 > 330) && (y + PANELSIZE / 2 < 360) && left) {//ワープの実装
            x = packman.Packman.PAINT_W - PANELSIZE / 2;
            y = 330;
        } else if (x + PANELSIZE / 2 > packman.Packman.PAINT_W && (y + PANELSIZE / 2 > 330) && (y + PANELSIZE / 2 < 360) && right) {//ワープの実装
            x = 0 - PANELSIZE / 2;
            y = 330;
        }
        if (y < 0) {
            y = 0;
        } else if (y + PANELSIZE > packman.Packman.PAINT_H) {
            y = packman.Packman.PAINT_H - PANELSIZE;
        }
        //壁にぶつかった場合の処理
        if (x >= 0 && x + PANELSIZE <= PAINT_W) {
            if (up && stagePanel[y / PANELSIZE][(x + PANELSIZE / 2) / PANELSIZE] == 0) {
                set(tempX, tempY);
            } else if (down && stagePanel[(y + PANELSIZE - 1) / PANELSIZE][(x + PANELSIZE / 2) / PANELSIZE] == 0) {
                set(tempX, tempY);
            } else if (left && stagePanel[(y + PANELSIZE / 2) / PANELSIZE][x / PANELSIZE] == 0) {
                set(tempX, tempY);
            } else if (right && stagePanel[(y + PANELSIZE / 2) / PANELSIZE][(x + PANELSIZE - 1) / PANELSIZE] == 0) {
                set(tempX, tempY);
            }
        }
        if (gameover || clear) {
            set(tempX, tempY);
        }
        tempX = (x + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
        tempY = (y + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
    }

    void packmanOnly() {
        if (x >= 0 && x + PANELSIZE <= PAINT_W) {
            //パワーエサ食べた時
            if (((stagePanel[(y + PANELSIZE / 2) / PANELSIZE][(x + PANELSIZE / 2) / PANELSIZE] >> shift.POWER.ordinal()) & 1) == 1) {
                powerCount = 0;
                power = true;
                firstPower = false;
                //ランダムに動く
                for (int k = 0; k < ENEMY; k++) {
                    enemy[k].ai.randRow = (enemy[k].y + PANELSIZE / 2) / PANELSIZE;
                    enemy[k].ai.randCol = (enemy[k].x + PANELSIZE / 2) / PANELSIZE;
                    enemy[k].ai.randomMove(k);
                }
            }
            if (((stagePanel[(y + PANELSIZE / 2) / PANELSIZE][(x + PANELSIZE / 2) / PANELSIZE] >> shift.DOT.ordinal()) & 1) == 1) {
                //ドットを食べる
                stagePanel[(y + PANELSIZE / 2) / PANELSIZE][(x + PANELSIZE / 2) / PANELSIZE] &= 0xF;
                dotSound.play();
            }
        }
        //クリア判定
        clear = true;
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 19; j++) {
                if (((stagePanel[i][j] >> shift.DOT.ordinal()) & 1) == 1) {
                    clear = false;
                    return;
                }
            }
        }
        if (clear) {
            powerSound.stop();
        }
    }

    //敵だけが必要な関数
    void enemyOnly(int k) {
        if (back) {
            ai.enemyBack(k);
        } else if (power) {
            ai.randomMove(k);
        }
        //敵と重なった時
        if ((x + PANELSIZE / 2) / PANELSIZE == (player.x + PANELSIZE / 2) / PANELSIZE
                && (y + PANELSIZE / 2) / PANELSIZE == (player.y + PANELSIZE / 2) / PANELSIZE) {
            if (power && !back) {
                back = true;
                eatSound.play();
            } else if (!back) {
                if (firstEnd) {
                    endSound.play();
                }
                gameover = true;
                firstEnd = false;
            }
        }
        //巣に帰ったら
        if ((x + PANELSIZE / 2) / PANELSIZE == 9 && (y + PANELSIZE / 2) / PANELSIZE == 10) {
            if (k == 0) {
                ai.reset(k);
            } else {
                enemy[k].ai.randRow = 10;
                enemy[k].ai.randCol = 9;
                ai.randomMove(k);
            }
            back = false;
        }
    }

    //移動
    void move() {
        if (up) {
            y -= 1;
        } else if (down) {
            y += 1;
        } else if (left) {
            x -= 1;
        } else if (right) {
            x += 1;
        }
    }
}
