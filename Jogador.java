
import java.awt.Color;
import java.awt.Graphics;

public class Jogador {

    int x = 100;
    int y = 100;

    int largura = 30;
    int altura = 30;

    int velocidade = 5;

    public void desenhar(Graphics g) {

        g.setColor(Color.BLUE);

        g.fillRect(x, y, largura, altura);

    }

    public void mover(int dx, int dy) {

        x += dx * velocidade;
        y += dy * velocidade;

    }
}
