package packman;

import packman.AstarRun.*;
import static packman.Chara.*;
import static packman.Packman.*;
import java.util.Random;

public class EnemyAI {

    Random rnd;
    AstarRun a;
    public int randRow = 10, randCol = 9;

    public EnemyAI(int k) {
        rnd = new Random();
        a = new AstarRun(22, 19);
        reset(k);
    }

    /**
     * @param k �G�̔ԍ�
     */
    //�ǐՃA���S���Y���̃��Z�b�g
    void reset(int k) {
        if (enemy[k].x < 0 || enemy[k].x + PANELSIZE > PAINT_W || player.x < 0 || player.x + PANELSIZE > PAINT_W) {
            return;
        }
        a.reset();
        a.start((player.y + PANELSIZE / 2) / PANELSIZE, (player.x + PANELSIZE / 2) / PANELSIZE);
        a.end((enemy[k].y + PANELSIZE / 2) / PANELSIZE, (enemy[k].x + PANELSIZE / 2) / PANELSIZE);
        setWall();
        a.run();
    }

    //�ǂ̃Z�b�g
    void setWall() {
        //�ǂ̃Z�b�e�B���O
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 19; j++) {
                if (stagePanel[i][j] == 0) {
                    a.wall(i, j);
                }
            }
        }
        //�n���f(�G���ʂ�Ȃ��ꏊ)
        a.wall(6, 8);
        a.wall(6, 10);
    }

    //�m�I�ȓG������
    void AI_Move(int k) {
        //�p�b�N�}������������
        if ((player.x >= 0 && player.x + PANELSIZE <= PAINT_W)
                && (((player.x + PANELSIZE / 2) / PANELSIZE * PANELSIZE != player.tempX
                || (player.y + PANELSIZE / 2) / PANELSIZE * PANELSIZE != player.tempY))) {
            if (!power) {
                enemy[k].ai.reset(k);
            }
        }

    }

    //�m�I�łȂ��G������
    void randomMove(int k) {
        if (enemy[k].x < 0 || enemy[k].x + PANELSIZE > PAINT_W
                || randRow != (enemy[k].y + PANELSIZE / 2) / PANELSIZE || randCol != (enemy[k].x + PANELSIZE / 2) / PANELSIZE) {
            return;
        }
        a.reset();
        while (true) {
            randRow = rnd.nextInt(22);
            randCol = rnd.nextInt(19);
            //�ǂ�n���f�̂Ƃ���ɗ����������x
            if (stagePanel[randRow][randCol] != 0 && !(randRow == 6 && (randCol == 8 || randCol == 10))) {
                a.start(randRow, randCol);
                break;
            }
        }
        a.end((enemy[k].y + PANELSIZE / 2) / PANELSIZE, (enemy[k].x + PANELSIZE / 2) / PANELSIZE);
        setWall();
        a.run();
    }

    //���ɖ߂�֐�
    void enemyBack(int k) {
        if (enemy[k].x < 0 || enemy[k].x + PANELSIZE > PAINT_W || player.x < 0 || player.x + PANELSIZE > PAINT_W) {
            return;
        }
        a.reset();
        a.start(10, 9);//���֖߂�

        a.end((enemy[k].y + PANELSIZE / 2) / PANELSIZE, (enemy[k].x + PANELSIZE / 2) / PANELSIZE);
        setWall();
        a.run();
    }

    //�G�̑���
    void flagControl(int k) {
        if (enemy[k].x < 0 || enemy[k].x + PANELSIZE > PAINT_W) {
            return;
        }
        if (a.z2[(enemy[k].y + PANELSIZE / 2) / PANELSIZE][(enemy[k].x + PANELSIZE / 2) / PANELSIZE].direction == Direction.UP.ordinal()) {
            enemy[k].flagset(true, false, false, false);
            enemy[k].x = (enemy[k].x + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
        } else if (a.z2[(enemy[k].y + PANELSIZE / 2) / PANELSIZE][(enemy[k].x + PANELSIZE / 2) / PANELSIZE].direction == Direction.DOWN.ordinal()) {
            enemy[k].flagset(false, true, false, false);
            enemy[k].x = (enemy[k].x + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
        } else if (a.z2[(enemy[k].y + PANELSIZE / 2) / PANELSIZE][(enemy[k].x + PANELSIZE / 2) / PANELSIZE].direction == Direction.LEFT.ordinal()) {
            enemy[k].flagset(false, false, true, false);
            enemy[k].y = (enemy[k].y + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
        } else if (a.z2[(enemy[k].y + PANELSIZE / 2) / PANELSIZE][(enemy[k].x + PANELSIZE / 2) / PANELSIZE].direction == Direction.RIGHT.ordinal()) {
            enemy[k].flagset(false, false, false, true);
            enemy[k].y = (enemy[k].y + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
        }
    }
}
