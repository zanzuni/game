import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Jogo extends JPanel implements ActionListener, KeyListener, MouseListener {

    private static final int LARGURA_MAPA = 1920;
    private static final int ALTURA_MAPA = 1080;

    private final Jogador jogador = new Jogador();
    private final Timer timer;
    private final CardLayout cards = new CardLayout();
    private final JPanel telas = new JPanel(cards);
    private final JFrame janela = new JFrame("Survival Island");

    private boolean cima, baixo, esquerda, direita;
    private boolean jogoIniciado;
    private boolean jogoTerminado;
    private boolean venceu;

    private String idioma = "PT";
    private int dificuldadeQuantidade = 10;
    private String dificuldadeNome = "Fácil";
    private String dificuldadeTipo = "FACIL";
    private String modoJogo = "NORMAL";
    private int pontos;
    private int contadorSpawn;

    private Rectangle caixaMunicao = new Rectangle(400, 300, 24, 24);
    private boolean caixaMunicaoColetada;
    private int quantidadeMunicao = 20;
    private final ArrayList<Rectangle> caixasMunicao = new ArrayList<Rectangle>();
    private final ArrayList<Boolean> caixasColetadas = new ArrayList<Boolean>();

    private final ArrayList<Tiro> tiros = new ArrayList<Tiro>();
    private final ArrayList<Inimigo> inimigos = new ArrayList<Inimigo>();
    private final Random random = new Random();

    public Jogo() {
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        JPanel menu = criarMenu();
        JPanel opcoes = criarOpcoes();

        telas.add(menu, "MENU");
        telas.add(opcoes, "OPCOES");
        telas.add(this, "JOGO");

        janela.setTitle("Survival Island");
        janela.setSize(LARGURA_MAPA, ALTURA_MAPA);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        janela.setLocationRelativeTo(null);
        janela.add(telas);
        janela.addKeyListener(this);
        janela.setVisible(true);

        cards.show(telas, "MENU");

        timer = new Timer(16, this);
        timer.start();
    }

    private JPanel criarMenu() {
        JPanel menu = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 55, 45));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(40, 100, 75));
                g2.fillRect(0, 0, getWidth(), 300);
            }
        };

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel();
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 64));
        titulo.setForeground(Color.WHITE);
        c.gridy = 0;
        menu.add(titulo, c);

        JLabel subtitulo = new JLabel();
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 22));
        subtitulo.setForeground(new Color(210, 235, 220));
        c.gridy = 1;
        menu.add(subtitulo, c);

        JButton iniciar = criarBotao("");
        c.gridy = 2;
        menu.add(iniciar, c);

        JButton opcoes = criarBotao("");
        c.gridy = 3;
        menu.add(opcoes, c);

        JButton sair = criarBotao("");
        c.gridy = 4;
        menu.add(sair, c);

        JButton divertido = criarBotao("");
        c.gridy = 5;
        menu.add(divertido, c);

        JButton impossivel = criarBotao("");
        c.gridy = 6;
        menu.add(impossivel, c);

        iniciar.addActionListener(e -> iniciarJogo());
        opcoes.addActionListener(e -> cards.show(telas, "OPCOES"));
        sair.addActionListener(e -> System.exit(0));
        divertido.addActionListener(e -> iniciarModoDivertido());
        impossivel.addActionListener(e -> iniciarModoImpossivel());

        atualizarRotulosMenu(titulo, subtitulo, iniciar, opcoes, sair,
                divertido, impossivel);
        return menu;
    }

    private JPanel criarOpcoes() {
        JPanel opcoes = new JPanel(new GridBagLayout());
        opcoes.setBackground(new Color(30, 45, 55));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20, 20, 30, 20);

        JLabel titulo = new JLabel();
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(Color.WHITE);
        opcoes.add(titulo, c);

        c.gridwidth = 1;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 10, 10);

        JLabel dificuldade = new JLabel();
        dificuldade.setFont(new Font("Arial", Font.BOLD, 24));
        dificuldade.setForeground(Color.WHITE);
        c.gridx = 0;
        opcoes.add(dificuldade, c);

        JButton facil = criarBotao("");
        JButton medio = criarBotao("");
        JButton dificil = criarBotao("");
        c.gridy = 2;
        c.gridx = 0;
        opcoes.add(facil, c);
        c.gridx = 1;
        opcoes.add(medio, c);
        c.gridx = 2;
        opcoes.add(dificil, c);

        facil.addActionListener(e -> selecionarDificuldade(10, "Fácil", "Easy"));
        medio.addActionListener(e -> selecionarDificuldade(20, "Médio", "Medium"));
        dificil.addActionListener(e -> selecionarDificuldade(30, "Difícil", "Hard"));

        c.gridy = 3;
        c.gridx = 0;
        JLabel idiomaLabel = new JLabel();
        idiomaLabel.setFont(new Font("Arial", Font.BOLD, 24));
        idiomaLabel.setForeground(Color.WHITE);
        opcoes.add(idiomaLabel, c);

        JButton portugues = criarBotao("");
        JButton ingles = criarBotao("");
        c.gridy = 4;
        c.gridx = 0;
        opcoes.add(portugues, c);
        c.gridx = 1;
        opcoes.add(ingles, c);

        JButton voltar = criarBotao("");
        c.gridy = 5;
        c.gridx = 1;
        opcoes.add(voltar, c);

        portugues.addActionListener(e -> alterarIdioma("PT"));
        ingles.addActionListener(e -> alterarIdioma("EN"));
        voltar.addActionListener(e -> cards.show(telas, "MENU"));

        atualizarRotulosOpcoes(opcoes, titulo, dificuldade, facil, medio,
                dificil, idiomaLabel, portugues, ingles, voltar);

        return opcoes;
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(260, 70));
        botao.setFont(new Font("Arial", Font.BOLD, 24));
        botao.setForeground(Color.WHITE);
        botao.setBackground(new Color(60, 130, 100));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder());
        return botao;
    }

    private void atualizarRotulosMenu(JLabel titulo, JLabel subtitulo,
                                      JButton iniciar, JButton opcoes,
                                      JButton sair, JButton divertido,
                                      JButton impossivel) {
        if (idioma.equals("PT")) {
            titulo.setText("SURVIVAL ISLAND");
            subtitulo.setText("Sobreviva e elimine os inimigos");
            iniciar.setText("Começar");
            opcoes.setText("Opções");
            sair.setText("Sair");
            divertido.setText("Modo Divertido");
            impossivel.setText("Modo Impossível");
        } else {
            titulo.setText("SURVIVAL ISLAND");
            subtitulo.setText("Survive and defeat the enemies");
            iniciar.setText("Start");
            opcoes.setText("Options");
            sair.setText("Exit");
            divertido.setText("Fun Mode");
            impossivel.setText("Impossible Mode");
        }
    }

    private void atualizarRotulosOpcoes(JPanel painel, JLabel titulo,
                                        JLabel dificuldade, JButton facil,
                                        JButton medio, JButton dificil,
                                        JLabel idiomaLabel, JButton portugues,
                                        JButton ingles, JButton voltar) {
        if (idioma.equals("PT")) {
            titulo.setText("OPÇÕES");
            dificuldade.setText("Dificuldade");
            facil.setText("Fácil - 10");
            medio.setText("Médio - 20");
            dificil.setText("Difícil - 30");
            idiomaLabel.setText("Idioma");
            portugues.setText("Português");
            ingles.setText("English");
            voltar.setText("Voltar");
        } else {
            titulo.setText("OPTIONS");
            dificuldade.setText("Difficulty");
            facil.setText("Easy - 10");
            medio.setText("Medium - 20");
            dificil.setText("Hard - 30");
            idiomaLabel.setText("Language");
            portugues.setText("Portuguese");
            ingles.setText("English");
            voltar.setText("Back");
        }

        for (Component componente : painel.getComponents()) {
            if (componente instanceof JButton) {
                ((JButton) componente).setText(((JButton) componente).getText());
            }
        }
    }

    private void alterarIdioma(String novoIdioma) {
        idioma = novoIdioma;
        atualizarNomeDificuldade();

        JPanel novoMenu = criarMenu();
        JPanel novasOpcoes = criarOpcoes();
        telas.removeAll();
        telas.add(novoMenu, "MENU");
        telas.add(novasOpcoes, "OPCOES");
        telas.add(this, "JOGO");
        cards.show(telas, "OPCOES");
        telas.revalidate();
        telas.repaint();
    }

    private void selecionarDificuldade(int quantidade,
                                       String nomePt, String nomeEn) {
        dificuldadeQuantidade = quantidade;
        dificuldadeNome = idioma.equals("PT") ? nomePt : nomeEn;
        dificuldadeTipo = quantidade == 10 ? "FACIL"
                : quantidade == 20 ? "MEDIO" : "DIFICIL";
    }

    private void atualizarNomeDificuldade() {
        if (dificuldadeTipo.equals("FACIL")) {
            dificuldadeNome = idioma.equals("PT") ? "Fácil" : "Easy";
        } else if (dificuldadeTipo.equals("MEDIO")) {
            dificuldadeNome = idioma.equals("PT") ? "Médio" : "Medium";
        } else {
            dificuldadeNome = idioma.equals("PT") ? "Difícil" : "Hard";
        }
    }

    private void iniciarJogo() {
        modoJogo = "NORMAL";
        pontos = 0;
        contadorSpawn = 0;
        jogador.x = 100;
        jogador.y = 100;
        cima = baixo = esquerda = direita = false;
        caixaMunicao = new Rectangle(400, 300, 24, 24);
        caixaMunicaoColetada = false;
        caixasMunicao.clear();
        caixasColetadas.clear();
        quantidadeMunicao = dificuldadeQuantidade * 3;
        tiros.clear();
        inimigos.clear();
        jogoIniciado = true;
        jogoTerminado = false;
        venceu = false;
        criarInimigos(dificuldadeQuantidade);
        cards.show(telas, "JOGO");
        requestFocusInWindow();
    }

    private void iniciarModoDivertido() {
        modoJogo = "DIVERTIDO";
        pontos = 0;
        contadorSpawn = 0;
        jogador.x = 100;
        jogador.y = 100;
        cima = baixo = esquerda = direita = false;
        caixaMunicaoColetada = true;
        caixasMunicao.clear();
        caixasColetadas.clear();
        quantidadeMunicao = 50;
        tiros.clear();
        inimigos.clear();
        jogoIniciado = true;
        jogoTerminado = false;
        venceu = false;
        criarInimigos(5);
        cards.show(telas, "JOGO");
        requestFocusInWindow();
    }

    private void iniciarModoImpossivel() {
        modoJogo = "IMPOSSIVEL";
        pontos = 0;
        contadorSpawn = 0;
        jogador.x = 100;
        jogador.y = 100;
        cima = baixo = esquerda = direita = false;
        quantidadeMunicao = 100;
        tiros.clear();
        inimigos.clear();
        caixasMunicao.clear();
        caixasColetadas.clear();

        caixasMunicao.add(new Rectangle(400, 300, 24, 24));
        caixasMunicao.add(new Rectangle(1500, 800, 24, 24));
        caixasColetadas.add(false);
        caixasColetadas.add(false);

        jogoIniciado = true;
        jogoTerminado = false;
        venceu = false;
        criarInimigos(30);
        cards.show(telas, "JOGO");
        requestFocusInWindow();
    }

    private void criarInimigos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            int x;
            int y;
            do {
                x = 100 + random.nextInt(LARGURA_MAPA - 200);
                y = 100 + random.nextInt(ALTURA_MAPA - 200);
            } while (Math.abs(x - jogador.x) < 250 &&
                    Math.abs(y - jogador.y) < 250);

            inimigos.add(new Inimigo(x, y));
        }
    }

    private void criarUmInimigo() {
        int lado = random.nextInt(4);
        int x;
        int y;

        if (lado == 0) {
            x = 50;
            y = 50 + random.nextInt(ALTURA_MAPA - 150);
        } else if (lado == 1) {
            x = LARGURA_MAPA - 50;
            y = 50 + random.nextInt(ALTURA_MAPA - 150);
        } else if (lado == 2) {
            x = random.nextInt(LARGURA_MAPA - 100);
            y = 50;
        } else {
            x = random.nextInt(LARGURA_MAPA - 100);
            y = ALTURA_MAPA - 50;
        }

        inimigos.add(new Inimigo(x, y));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(90, 160, 80));
        g.fillRect(0, 0, LARGURA_MAPA, ALTURA_MAPA);

        g.setColor(new Color(50, 140, 200));
        g.fillRect(0, 0, LARGURA_MAPA, 50);

        jogador.desenhar(g);

        if (!caixaMunicaoColetada && !modoJogo.equals("IMPOSSIVEL")) {
            g.setColor(Color.YELLOW);
            g.fillRect(caixaMunicao.x, caixaMunicao.y,
                    caixaMunicao.width, caixaMunicao.height);
        }

        if (modoJogo.equals("IMPOSSIVEL")) {
            g.setColor(Color.YELLOW);
            for (int i = 0; i < caixasMunicao.size(); i++) {
                if (!caixasColetadas.get(i)) {
                    Rectangle caixa = caixasMunicao.get(i);
                    g.fillRect(caixa.x, caixa.y, caixa.width, caixa.height);
                }
            }
        }

        g.setColor(Color.RED);
        for (Tiro tiro : tiros) {
            g.fillOval(tiro.x, tiro.y, 8, 8);
        }

        for (Inimigo inimigo : inimigos) {
            inimigo.desenhar(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("SURVIVAL ISLAND", 20, 75);
        g.drawString((idioma.equals("PT") ? "Munição: " : "Ammo: ") +
                (modoJogo.equals("DIVERTIDO") ? "∞" : quantidadeMunicao),
                20, 100);
        g.drawString((idioma.equals("PT") ? "Inimigos: " : "Enemies: ") +
                inimigos.size(), 20, 125);
        g.drawString((idioma.equals("PT") ? "Dificuldade: " : "Difficulty: ") +
                dificuldadeNome, 20, 150);
        if (!modoJogo.equals("NORMAL")) {
            g.drawString((idioma.equals("PT") ? "Pontos: " : "Score: ") +
                    pontos, 20, 175);
            g.drawString(modoJogo.equals("DIVERTIDO")
                    ? (idioma.equals("PT") ? "Modo Divertido" : "Fun Mode")
                    : (idioma.equals("PT") ? "Modo Impossível" : "Impossible Mode"),
                    20, 200);
        }

        if (jogoTerminado) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, LARGURA_MAPA, ALTURA_MAPA);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 64));
            String mensagem = venceu
                    ? (idioma.equals("PT") ? "VOCÊ VENCEU!" : "YOU WIN!")
                    : (idioma.equals("PT") ? "FIM DE JOGO" : "GAME OVER");
            int largura = g.getFontMetrics().stringWidth(mensagem);
            g.drawString(mensagem, (LARGURA_MAPA - largura) / 2, 500);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            String voltar = idioma.equals("PT")
                    ? "Pressione ESC para voltar ao menu"
                    : "Press ESC to return to the menu";
            largura = g.getFontMetrics().stringWidth(voltar);
            g.drawString(voltar, (LARGURA_MAPA - largura) / 2, 550);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!jogoIniciado || jogoTerminado) {
            return;
        }

        if (cima) jogador.mover(0, -1);
        if (baixo) jogador.mover(0, 1);
        if (esquerda) jogador.mover(-1, 0);
        if (direita) jogador.mover(1, 0);

        if (jogador.x < 0) jogador.x = 0;
        if (jogador.y < 50) jogador.y = 50;
        if (jogador.x > LARGURA_MAPA - jogador.largura)
            jogador.x = LARGURA_MAPA - jogador.largura;
        if (jogador.y > ALTURA_MAPA - jogador.altura)
            jogador.y = ALTURA_MAPA - jogador.altura;

        Rectangle retanguloJogador = new Rectangle(
                jogador.x, jogador.y, jogador.largura, jogador.altura);

        if (!modoJogo.equals("IMPOSSIVEL") &&
                !caixaMunicaoColetada &&
                retanguloJogador.intersects(caixaMunicao)) {
            caixaMunicaoColetada = true;
                quantidadeMunicao += 10;
        }

        if (modoJogo.equals("IMPOSSIVEL")) {
            for (int i = 0; i < caixasMunicao.size(); i++) {
                if (!caixasColetadas.get(i) &&
                        retanguloJogador.intersects(caixasMunicao.get(i))) {
                    caixasColetadas.set(i, true);
                    quantidadeMunicao += 20;
                }
            }
        }

        atualizarTiros();
        atualizarInimigos(retanguloJogador);

        if (modoJogo.equals("DIVERTIDO")) {
            contadorSpawn++;
            if (contadorSpawn >= 45) {
                contadorSpawn = 0;
                criarUmInimigo();
            }
        }

        if (!jogoTerminado && !modoJogo.equals("DIVERTIDO") &&
                inimigos.isEmpty()) {
            venceu = true;
            jogoTerminado = true;
        }

        repaint();
    }

    private void atualizarTiros() {
        for (int i = tiros.size() - 1; i >= 0; i--) {
            Tiro tiro = tiros.get(i);
            tiro.mover();

            boolean acertou = false;
            for (int j = inimigos.size() - 1; j >= 0; j--) {
                Inimigo inimigo = inimigos.get(j);
                if (tiro.colideCom(inimigo)) {
                    inimigo.vida--;
                    acertou = true;
                    if (inimigo.vida <= 0) {
                        inimigos.remove(j);
                        pontos += 100;
                    }
                    break;
                }
            }

            if (acertou ||
                    tiro.x < 0 || tiro.x > LARGURA_MAPA ||
                    tiro.y < 0 || tiro.y > ALTURA_MAPA) {
                tiros.remove(i);
            }
        }
    }

    private void atualizarInimigos(Rectangle retanguloJogador) {
        for (Inimigo inimigo : inimigos) {
            inimigo.seguir(jogador.x, jogador.y);
            if (retanguloJogador.intersects(inimigo.retangulo())) {
                venceu = false;
                jogoTerminado = true;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_ESCAPE && jogoTerminado) {
            jogoIniciado = false;
            cards.show(telas, "MENU");
            return;
        }

        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) cima = true;
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) baixo = true;
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) esquerda = true;
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) direita = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) cima = false;
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) baixo = false;
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) esquerda = false;
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) direita = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!jogoIniciado || jogoTerminado ||
                e.getButton() != MouseEvent.BUTTON1 ||
                (!modoJogo.equals("DIVERTIDO") && quantidadeMunicao <= 0)) {
            return;
        }

        int origemX = jogador.x + jogador.largura / 2;
        int origemY = jogador.y + jogador.altura / 2;

        double distanciaX = e.getX() - origemX;
        double distanciaY = e.getY() - origemY;
        double distancia = Math.sqrt(
                distanciaX * distanciaX + distanciaY * distanciaY);

        if (distancia == 0) {
            return;
        }

        double direcaoX = distanciaX / distancia;
        double direcaoY = distanciaY / distancia;
        tiros.add(new Tiro(origemX, origemY, direcaoX, direcaoY));
        if (!modoJogo.equals("DIVERTIDO")) {
            quantidadeMunicao--;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private class Tiro {
        int x;
        int y;
        final double direcaoX;
        final double direcaoY;
        final int velocidade = 12;

        Tiro(int x, int y, double direcaoX, double direcaoY) {
            this.x = x;
            this.y = y;
            this.direcaoX = direcaoX;
            this.direcaoY = direcaoY;
        }

        void mover() {
            x += (int) (direcaoX * velocidade);
            y += (int) (direcaoY * velocidade);
        }

        boolean colideCom(Inimigo inimigo) {
            return new Rectangle(x, y, 8, 8).intersects(inimigo.retangulo());
        }
    }

    private class Inimigo {
        int x;
        int y;
        final int largura = 32;
        final int altura = 32;
        int vida = modoJogo.equals("IMPOSSIVEL") ? 4 : 3;
        final int velocidade = modoJogo.equals("IMPOSSIVEL") ? 2 : 1;

        Inimigo(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void seguir(int jogadorX, int jogadorY) {
            if (x < jogadorX) x += velocidade;
            if (x > jogadorX) x -= velocidade;
            if (y < jogadorY) y += velocidade;
            if (y > jogadorY) y -= velocidade;
        }

        Rectangle retangulo() {
            return new Rectangle(x, y, largura, altura);
        }

        void desenhar(Graphics g) {
            g.setColor(new Color(150, 45, 45));
            g.fillRect(x, y, largura, altura);

            g.setColor(Color.WHITE);
            g.fillRect(x, y - 8, largura, 5);
            g.setColor(Color.GREEN);
            int vidaMaxima = modoJogo.equals("IMPOSSIVEL") ? 4 : 3;
            g.fillRect(x, y - 8, largura * vida / vidaMaxima, 5);
        }
    }
}
