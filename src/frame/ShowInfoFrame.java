package frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import pojo.Communication;
import pojo.Customer;
import com.mr.contact.dao.Dao;
import com.mr.contact.dao.DaoFactory;
import com.mr.contact.swing.CustomerFrame;
import com.mr.contact.swing.FixedTable;
import pojo.User;

public class ShowInfoFrame extends CustomerFrame
{
    private Dao dao;
    private MainFrame frame;
    private Customer cust;
    private FixedTable table;
    private JTextField nameText;
    private JTextField workUnitText;
    private JTextField roleText;
    private JTextField workAddressText;
    private JTextField homeText;
    private JTextField birthText;
    private JTextField sexText;
    private DefaultTableModel tableModel;

    public ShowInfoFrame(Customer cust, JFrame frame)
    {
        super(frame,CustomerFrame.SHOW);
        this.cust = cust;
        this.frame = (MainFrame) frame;
        setTitle("详细信息");
        dao = DaoFactory.getDao();

        table = getTable();
        table.setCellEditable(false);

        tableModel = getTableModel();
        initTbleModel();

        nameText = getNameText();
        nameText.setText(cust.getName());

        sexText = getSexText();
        sexText.setText(cust.getSex());

        birthText = getBirthText();
        if (null != cust.getBirth())
        {
            birthText.setText(cust.getBirth());
        }

        workUnitText = getWorkUnitText();
        if (null != cust.getWork_unit())
        {
            workUnitText.setText(cust.getWork_unit());
        }

        roleText = getRoleText();
        if (null != cust.getRole())
        {
            roleText.setText(cust.getRole());
        }

        workAddressText = getWorkAddressText();
        if (null != cust.getWork_addr())
        {
            workAddressText.setText(cust.getWork_addr());
        }

        homeText = getHomeText();
        if (null != cust.getHome_addr())
        {
            homeText.setText(cust.getHome_addr());
        }

        FlowLayout btnPanelLayout = new FlowLayout(FlowLayout.RIGHT);
        JPanel btnPanel = new JPanel(btnPanelLayout);
        getContentPane().add(btnPanel,BorderLayout.SOUTH);
        JButton btnNewButton = new JButton("关闭");
        btnPanel.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
    }

    private void initTbleModel()
    {
        if (tableModel.getRowCount() > 0)
        {
            tableModel.getDataVector().clear();
            tableModel.fireTableDataChanged();
        }
        List<Communication> usableList = dao.selectCustmerCommunicationUsable(cust);
        String[] tableValues = new String[5];
        for (Communication com : usableList)
        {
            if(com.getAvailable().endsWith("Y"))
            {
                tableValues[0] = "" + com.getId();
                tableValues[1] = com.getOffice_phone();
                tableValues[2] = com.getMobile_phone();
                tableValues[3] = com.getEmail();
                tableValues[4] = com.getQq();
                tableModel.addRow(tableValues);
            }
        }
    }
}
