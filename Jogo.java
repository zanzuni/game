
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Jogo extends JPanel implements ActionListener, KeyListener {

    Jogador jogador = new Jogador();

    Timer timer;

    boolean cima, baixo, esquerda, direita;

    public Jogo() {

        setFocusable(true);
addKeyListener(this);

        JFrame janela = new JFrame("Survival Island");

        janela.setSize(1920, 1080);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        janela.add(this);

        janela.addKeyListener(this);

        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        timer = new Timer(16, this);
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Desenhar o chão
        g.setColor(new Color(90, 160, 80));
        g.fillRect(0, 0, 1920, 1080);

        // Desenhar a água
        g.setColor(new Color(50, 140, 200));
        g.fillRect(0, 0, 1920, 50);

        // Desenhar o jogador
        jogador.desenhar(g);

        // Nome do jogo
        g.setColor(Color.WHITE);
        g.drawString("SURVIVAL ISLAND", 20, 30);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cima) jogador.mover(0, -1);
        if (baixo) jogador.mover(0, 1);
        if (esquerda) jogador.mover(-1, 0);
        if (direita) jogador.mover(1, 0);

        // Limites do mapa
if (jogador.x < 0) jogador.x = 0;
if (jogador.y < 50) jogador.y = 50;

if (jogador.x > 1870) jogador.x = 1870;
if (jogador.y > 1030) jogador.y = 1030;

        // Atualizar a tela
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP)
            cima = true;

        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN)
            baixo = true;

        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT)
            esquerda = true;

        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT)
            direita = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP)
            cima = false;

        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN)
            baixo = false;

        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT)
            esquerda = false;

        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT)
            direita = false;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
