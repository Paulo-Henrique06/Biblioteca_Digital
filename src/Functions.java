import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class Functions {
    static JPanel selectedPanel = null;
    public static int bookID = -1;
    public static void addLib(JFrame parent){

        JDialog addWindow = new JDialog(parent, "Novo Livro", true);
        addWindow.setLayout(new BoxLayout(addWindow.getContentPane(), BoxLayout.Y_AXIS));

        JPanel panelName = new JPanel();
        panelName.setLayout(new FlowLayout());

        JLabel nameTitle = new JLabel("Nome");
        panelName.add(nameTitle);

        JTextField nameBar = new JTextField(15);
        nameBar.getText();
        panelName.add(nameBar);

        JPanel panelAuthor = new JPanel();
        panelAuthor.setLayout(new FlowLayout());

        JLabel nameAuthor = new JLabel("Autor");
        panelAuthor.add(nameAuthor);

        JTextField authorBar = new JTextField(15);
        panelAuthor.add(authorBar);

        JPanel panelTopo = new JPanel();
        panelTopo.setLayout(new FlowLayout());
        panelTopo.add(panelName);
        panelTopo.add(panelAuthor);

        JPanel panelSinopse = new JPanel();
        panelSinopse.setLayout(new BoxLayout(panelSinopse, BoxLayout.Y_AXIS));

        JLabel nameSinopse = new JLabel("Sinopse");
        nameSinopse.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSinopse.add(nameSinopse);

        JTextArea sinopseBar = new JTextArea(4,0);
        sinopseBar.setLineWrap(true);
        sinopseBar.setWrapStyleWord(true);
        JScrollPane scrollSinopse = new JScrollPane(sinopseBar);
        panelSinopse.add(scrollSinopse);

        JPanel panelPhoto = new JPanel();
        panelPhoto.setLayout(new BoxLayout(panelPhoto, BoxLayout.Y_AXIS));

        JButton buttonPhoto = new JButton("Escolha uma foto");
        buttonPhoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        AtomicReference<File> photoAdded = new AtomicReference<>();
        buttonPhoto.addActionListener(e -> {
            JFileChooser choosePhoto = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Imagens (JPG e PNG)", "png", "jpeg", "jpg"
            );
            choosePhoto.setFileFilter(filter);
            int option = choosePhoto.showOpenDialog(parent);
            if (option == JFileChooser.APPROVE_OPTION){
                photoAdded.set(choosePhoto.getSelectedFile());
                buttonPhoto.setText("Selecionado: " + photoAdded);
            }
        });

        panelPhoto.add(buttonPhoto);

        JPanel panelSaveCancel = new JPanel();
        panelSaveCancel.setLayout(new FlowLayout());


        JButton buttonSave = new JButton("Salvar");
        buttonSave.addActionListener(e -> {
            String name = nameBar.getText().trim();
            String author = authorBar.getText().trim();
            String sinopse = sinopseBar.getText().trim();
            String image = photoAdded.get() != null ? photoAdded.get().getAbsolutePath() : "";

            if (name.isEmpty()){
                JOptionPane.showMessageDialog(addWindow, "Preencha os Campos Obrigatórios",
                        "Campos Obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(addWindow, "Livro Salvo",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                addWindow.dispose();
                Database.addLivro(name,author,sinopse,image);
            }
        });
        panelSaveCancel.add(buttonSave);

        JButton buttonCancel = new JButton("Cancelar");
        buttonCancel.addActionListener(e -> {
            addWindow.dispose();
        });
        panelSaveCancel.add(buttonCancel);

        addWindow.add(panelTopo);
        addWindow.add(panelSinopse);
        addWindow.add(Box.createRigidArea(new Dimension(0,10)));
        addWindow.add(panelPhoto);
        addWindow.add(Box.createRigidArea(new Dimension(0, 10)));
        addWindow.add(panelSaveCancel);
        addWindow.pack();
        addWindow.setVisible(true);
    }
    public static void attLib(JPanel panelEsq, JPanel panelDir) {
        panelEsq.removeAll(); // Limpa o painel antes de atualizar

        ResultSet rs = Database.listarLivros();
        try {
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String autor = rs.getString("autor");
                String sinopse = rs.getString("sinopse");
                String image = rs.getString("imagem");

                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (itemPanel != selectedPanel){
                            itemPanel.setBackground(new Color(173, 216, 230));
                        }
                    }

                    public void mouseExited(MouseEvent e) {
                        if(bookID != id) {
                            itemPanel.setBackground(Color.WHITE);
                        }
                    }
                    public void mouseClicked(MouseEvent e){
                        selectedPanel = itemPanel;
                        if (selectedPanel != null) {
                            selectedPanel.setBackground(Color.WHITE);
                        }
                        bookID = id;
                        panelDir.removeAll();
                        panelDir.setLayout(new BoxLayout(panelDir, BoxLayout.Y_AXIS));

                        JLabel labelName = new JLabel("Nome: " + nome);
                        JLabel labelAuthor = new JLabel("Autor: " + autor);
                        JLabel labelSinopseTittle = new JLabel("Sinopse:");
                        JTextArea labelSinopse = new JTextArea(sinopse);
                        labelSinopse.setLineWrap(true);
                        labelSinopse.setWrapStyleWord(true);
                        labelSinopse.setEditable(false);
                        labelSinopse.setBackground(panelDir.getBackground());

                        labelName.setAlignmentX(Component.LEFT_ALIGNMENT);
                        labelAuthor.setAlignmentX(Component.LEFT_ALIGNMENT);
                        labelSinopseTittle.setAlignmentX(Component.LEFT_ALIGNMENT);
                        labelSinopse.setAlignmentX(Component.LEFT_ALIGNMENT);

                        panelDir.add(labelName);
                        panelDir.add(labelAuthor);
                        panelDir.add(labelSinopseTittle);
                        panelDir.add(labelSinopse);

                        if (image != null && !image.isEmpty()) {
                            try {
                                ImageIcon icon = new ImageIcon(image);
                                Image img = icon.getImage();
                                Image newimg = img.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH);
                                icon = new ImageIcon(newimg);
                                JLabel labelImagem = new JLabel(icon);
                                panelDir.add(Box.createRigidArea(new Dimension(0, 10)));
                                panelDir.add(labelImagem);
                            } catch (Exception ex) {
                                System.out.println("Erro ao carregar imagem: " + ex.getMessage());
                            }
                        }
                        panelDir.revalidate();
                        panelDir.repaint();
                    }
                });

                JLabel nomeLabel = new JLabel("📖 " + nome);
                JLabel autorLabel = new JLabel("✍️ " + autor);

                itemPanel.add(nomeLabel);
                itemPanel.add(autorLabel);
                itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));

                panelEsq.add(itemPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panelEsq.revalidate();
        panelEsq.repaint();
    }

    public static void rmvLib(int id, JPanel panelEsq, JPanel panelDir) {
        if(bookID != -1){
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja excluir?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                bookID = -1;
                panelDir.removeAll();
                Database.removerLivro(id);
                attLib(panelEsq, panelDir);
                JOptionPane.showMessageDialog(null, "Livro Excluído");
            }
            else {}
        }
        else {
            JOptionPane.showMessageDialog(null, "Nenhum Livro Selecionado");
        }
    }

}
