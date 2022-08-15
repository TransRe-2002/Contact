package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.mr.contact.dao.*;
import com.mr.contact.swing.*;
import pojo.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class UpdateCustomerFrame extends CustomerFrame
{
    private final byte ALLSOUR = 0x5a;
    private final byte USABLESOUR = 0x1a;
    private Dao dao;
    private Customer cust;
    private MainFrame frame;
    private FixedTable table;
    private DefaultTableModel tableModel;
    private JTextField nameText;
    private JTextField workUnitText;
    private JTextField roleText;
    private JTextField workAddressText;
    private JTextField homeText;
    private JTextField birthText;
    private JButton cancelBtn;
    private JButton saveBtn;
    private JComboBox<String> sexComboBox;
    private JButton delRowBtn;
    private JButton addRowBtn;
    private JCheckBox chckbxNewCheckBox;

    public UpdateCustomerFrame(Customer cust, JFrame frame)
    {
        super(frame,CustomerFrame.UPDATE);
        this.cust = cust;
        this.frame = (MainFrame) frame;
        setTitle("修改客户信息");
        dao = DaoFactory.getDao();
        init();
    }

    private void init()
    {
        cust = dao.selectCustomer(cust.getId());
        setTableModelSource(USABLESOUR);
        table = getTable();
        table.setModel(tableModel);
        table.setCellEditable(true);
        table.hiddenFirstColumn();

        nameText = getNameText();
        nameText.setText(cust.getName());

        sexComboBox = getSexCombo();
        if (null == cust.getSex())
        {
            sexComboBox.setSelectedIndex(0);
        }
        else if (cust.getSex().equals("女"))
        {
            sexComboBox.setSelectedIndex(2);
        }
        else
        {
            sexComboBox.setSelectedIndex(1);
        }

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


    }

    private void setTableModelSource(byte type)
    {

    }
}
