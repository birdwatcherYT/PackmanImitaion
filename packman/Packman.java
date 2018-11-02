package packman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static packman.Chara.*;

public class Packman extends JFrame implements KeyListener {

    static final int W = 600, H = 700, PANELSIZE = 30;
    static final int PAINT_W = W - 30, PAINT_H = H - 40,
            ENEMY = 4, //�G�̐�
            POWER_TIME = 35;  //�p���[�G�T�������� 
    static int imgSelect, powerCount;
    static boolean gameover, clear, power, firstEnd, firstPower;
    static Chara player, enemy[];
    static AudioClip eatSound, dotSound, bgm, powerSound, endSound;
    static Image image;

    //�ǂꂾ���V�t�g���K�v��(stagePanel[i][j]�̉�6�r�b�g��[power][dot][up][down][left][right]�̏��Ƀt���O������)
    enum shift {

        RIGHT, LEFT, DOWN, UP, DOT, POWER
    }

    public static void main(String[] args) {
        new Packman();
    }

    Packman() {
        super("Packman");

        image = (new ImageIcon(getClass().getResource("stage.png"))).getImage();
        bgm = Applet.newAudioClip(getClass().getResource("obake.wav"));
        eatSound = Applet.newAudioClip(getClass().getResource("eat.wav"));
        dotSound = Applet.newAudioClip(getClass().getResource("dot.wav"));
        powerSound = Applet.newAudioClip(getClass().getResource("power.wav"));
        endSound = Applet.newAudioClip(getClass().getResource("end.wav"));
        bgm.loop();
        set();

        addKeyListener(this);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        JComponent jcom = new JComponentEx();
        jcom.setPreferredSize(new Dimension(PAINT_W, PAINT_H));
        c.add(jcom);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 20, W, H);
        setResizable(false);
        setVisible(true);

    }

    //������
    void set() {
        powerSound.stop();
        imgSelect = powerCount = 0;
        stageSet();
        player = new Chara();
        enemy = new Chara[ENEMY];
        for (int i = 0; i < ENEMY; ++i) {
            enemy[i] = new Chara();
            enemy[i].set(PAINT_W / 2 - PANELSIZE / 2, PAINT_H / 2 - PANELSIZE);
            enemy[i].flagset(false, false, false, false);
            enemy[i].ai = new EnemyAI(i);
        }
        player.flagset(false, false, true, false);
        player.set(PAINT_W / 2 - PANELSIZE / 2, PAINT_H - 5 * PANELSIZE);
        gameover = clear = power = false;
        firstPower = firstEnd = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.x >= 0 && player.x + PANELSIZE <= PAINT_W) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_I:
                case KeyEvent.VK_E:
                    if ((((stagePanel[(player.y + PANELSIZE / 2) / PANELSIZE][(player.x + PANELSIZE / 2) / PANELSIZE] >> shift.UP.ordinal()) & 1)) == 1) {
                        player.flagset(true, false, false, false);
                        player.x = (player.x + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_COMMA:
                case KeyEvent.VK_C:
                    if ((((stagePanel[(player.y + PANELSIZE / 2) / PANELSIZE][(player.x + PANELSIZE / 2) / PANELSIZE] >> shift.DOWN.ordinal()) & 1)) == 1) {
                        player.flagset(false, true, false, false);
                        player.x = (player.x + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_S:
                case KeyEvent.VK_J:
                    if ((((stagePanel[(player.y + PANELSIZE / 2) / PANELSIZE][(player.x + PANELSIZE / 2) / PANELSIZE] >> shift.LEFT.ordinal()) & 1)) == 1) {
                        player.flagset(false, false, true, false);
                        player.y = (player.y + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_L:
                case KeyEvent.VK_F:
                    if ((((stagePanel[(player.y + PANELSIZE / 2) / PANELSIZE][(player.x + PANELSIZE / 2) / PANELSIZE] >> shift.RIGHT.ordinal()) & 1)) == 1) {
                        player.flagset(false, false, false, true);
                        player.y = (player.y + PANELSIZE / 2) / PANELSIZE * PANELSIZE;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (gameover || clear) {
                        set();
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private static class JComponentEx extends JComponent implements Runnable {

        public JComponentEx() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                if (!gameover && !clear) {
                    player.move();
                    player.packmanOnly();
                    for (int i = 0; i < ENEMY; i++) {
                        if (firstPower && i == 3) {//4�̖ڂ̓p���[�G�T�H�ׂĂ���̓o��
                            continue;
                        }
                        //�p���[�A�b�v���͓G�̑��x1/2(imgSelect����̎��݈̂ړ�)
                        if (power && !enemy[i].back) {
                            if ((imgSelect & 1) == 1) {
                                enemy[i].move();
                            }
                        } else {
                            enemy[i].move();
                        }
                        enemy[i].stage();
                        enemy[i].enemyOnly(i);
                        if (i != 0) {//i=0�����͓��]�h
                            enemy[i].ai.randomMove(i);
                        } else {
                            enemy[i].ai.AI_Move(i);
                        }
                        enemy[i].ai.flagControl(i);
                    }
                    player.stage();

                    imgSelect++;
                    if (imgSelect == 30) {
                        imgSelect = 0;
                        if (power) {
                            powerSound.loop();
                            powerCount++;
                        }
                    }
                    //�p���[�G�T�̌��E��������
                    if (powerCount == POWER_TIME) {
                        powerCount = 0;
                        powerSound.stop();
                        power = false;
                    }
                }
                repaint();
                try {
                    Thread.sleep(8);
                } catch (InterruptedException err) {
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, this);

            //�h�b�g�`��
            g.setColor(Color.yellow);
            for (int i = 0; i < 22; i++) {
                for (int j = 0; j < 19; j++) {
                    //�p���[�G�T�̕`��
                    if (((stagePanel[i][j] >> shift.POWER.ordinal()) & 1) == 1) {
                        g.fillOval(j * PANELSIZE + PANELSIZE / 2 - 7, i * PANELSIZE + PANELSIZE / 2 - 7, 14, 14);
                    } else if (((stagePanel[i][j] >> shift.DOT.ordinal()) & 1) == 1) {//�h�b�g�̕`��
                        g.fillRect(j * PANELSIZE + PANELSIZE / 2 - 2, i * PANELSIZE + PANELSIZE / 2 - 2, 4, 4);
                    }
                }
            }
            //�p�b�N�}���`��
            if (power)
                g.setColor(Color.red);

            if (player.right) {
                if (imgSelect < 10) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 30, 300);
                } else if (10 <= imgSelect && imgSelect < 20) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 60, 240);
                } else if (20 <= imgSelect && imgSelect < 30) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 0, 360);
                }
            } else if (player.left) {
                if (imgSelect < 10) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 180 + 30, 300);
                } else if (10 <= imgSelect && imgSelect < 20) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 180 + 60, 240);
                } else if (20 <= imgSelect && imgSelect < 30) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 0, 360);
                }
            } else if (player.up) {
                if (imgSelect < 10) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 90 + 30, 300);
                } else if (10 <= imgSelect && imgSelect < 20) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 90 + 60, 240);
                } else if (20 <= imgSelect && imgSelect < 30) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 0, 360);
                }
            } else if (player.down) {
                if (imgSelect < 10) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, -90 + 30, 300);
                } else if (10 <= imgSelect && imgSelect < 20) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, -90 + 60, 240);
                } else if (20 <= imgSelect && imgSelect < 30) {
                    g.fillArc(player.x, player.y, PANELSIZE, PANELSIZE, 0, 360);
                }
            }
            //�G�̕`��
            for (int i = 0; i < ENEMY; i++) {
                //��
                if (!enemy[i].back) {
                    switch (i) {
                        case 0:
                            g.setColor(Color.red);
                            break;
                        case 1:
                            g.setColor(Color.cyan);
                            break;
                        case 2:
                            g.setColor(Color.green);
                            break;
                        case 3:
                            g.setColor(Color.orange);
                            break;
                    }
                    if (power) {
                        g.setColor(Color.blue);
                    }
                    g.fillRect(enemy[i].x, enemy[i].y, PANELSIZE, PANELSIZE);
                    //�̂̐�
                    g.setColor(Color.black);
                    for (int k = 1; k < 6; k++) {
                        g.drawLine(enemy[i].x + 5 * k, enemy[i].y + PANELSIZE * 3 / 4, enemy[i].x + 5 * k, enemy[i].y + PANELSIZE);
                    }
                }
                //�ڂ̐F
                if (power) {
                    g.setColor(Color.white);
                } else {
                    g.setColor(Color.black);
                }
                //��
                g.fillRect(enemy[i].x + PANELSIZE / 4 - 2, enemy[i].y - 5 + PANELSIZE / 2, 5, 5);
                g.fillRect(enemy[i].x + PANELSIZE * 3 / 4 - 2, enemy[i].y - 5 + PANELSIZE / 2, 5, 5);
            }
            if (clear) {
                g.setColor(Color.white);
                g.setFont(new Font(null, Font.BOLD, 60));
                g.drawString("�N���A", PAINT_W / 2 - 100, PAINT_H / 2);
            } else if (gameover) {
                g.setColor(Color.red);
                g.setFont(new Font(null, Font.BOLD, 60));
                g.drawString("GAME OVER", PAINT_W / 2 - 180, PAINT_H / 2);
            }
        }
    }
}
