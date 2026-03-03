import javax.swing.*;
import java.awt.*;

public class Interface {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Biblioteca");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelTopo = new JPanel(new FlowLayout());
        panelTopo.setBackground(Color.gray);

        JPanel panelEsq = new JPanel();
        panelEsq.setLayout(new GridLayout(0, 1, 0, 5));
        panelEsq.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(panelEsq);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        JPanel panelDir = new JPanel(new FlowLayout());
        panelDir.setBackground(Color.gray);

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, panelDir);

        JButton buttonAdd = new JButton("Adicionar");
        buttonAdd.addActionListener(e -> {
            Functions.addLib(frame);

            Functions.attLib(panelEsq, panelDir);
        });
        panelTopo.add(buttonAdd);

        JButton buttonEdit = new JButton("Editar");
        panelTopo.add(buttonEdit);

        JButton buttonRmv = new JButton("Remover");
        panelTopo.add(buttonRmv);
        buttonRmv.addActionListener(e -> {
            Functions.rmvLib(Functions.bookID, panelEsq, panelDir);
        });

        frame.add(panelTopo, BorderLayout.NORTH);
        frame.add(division, BorderLayout.CENTER);


        Functions.attLib(panelEsq, panelDir);

        frame.setVisible(true);
    }
}




