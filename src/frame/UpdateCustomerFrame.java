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
        addAction();
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

        chckbxNewCheckBox = getChckbxNewCheckBox();

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1,2));
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        FlowLayout p4layout = new FlowLayout();
        p4layout.setHgap(20);

        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.setLayout(p4layout);
        southPanel.add(leftButtonPanel);

        delRowBtn = new JButton("删除行");
        leftButtonPanel.add(delRowBtn);

        addRowBtn = new JButton("添加行");
        leftButtonPanel.add(addRowBtn);

        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.setLayout(p4layout);
        southPanel.add(rightButtonPanel);

        saveBtn = new JButton("保存");
        rightButtonPanel.add(saveBtn);

        cancelBtn = new JButton("取消");
        rightButtonPanel.add(cancelBtn);
    }

    private void setTableModelSource(byte type)
    {
        if (tableModel == null)
        {
            String[] columnNames = {"编号","办公电话","移动电话","电子邮箱","QQ","是否有效"};
            tableModel = new DefaultTableModel();
            tableModel.setColumnIdentifiers(columnNames);
        }

        if (tableModel.getRowCount() > 0)
        {
            tableModel.getDataVector().clear();
            tableModel.fireTableDataChanged();
        }

        List<Communication> usableList = null;
        if (type == ALLSOUR)
        {
            usableList = dao.selectCustmerCommunicationAll(cust);
        }
        else if (type == USABLESOUR)
        {
            usableList = dao.selectCustmerCommunicationUsable(cust);
        }
        else
        {
            return;
        }

        int comCount = usableList.size();
        String[] tableValues = new String[6];
        for (int i = 0; i < comCount; i++)
        {
            Communication com = usableList.get(i);
            tableValues[0] = "" + com.getId();
            tableValues[1] = com.getOffice_phone();
            tableValues[2] = com.getMobile_phone();
            tableValues[3] = com.getEmail();
            tableValues[4] = com.getQq();
            tableValues[5] = com.getAvailable();
            tableModel.addRow(tableValues);
        }
    }

    public void dispose()
    {
        super.dispose();
        frame.initTable();
    }

    private void addAction()
    {
        cancelBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        addRowBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Communication addCom = new Communication();
                addCom.setCust(cust);
                addCom.setAvailable("Y");

                dao.addCommunication(addCom,MainFrame.getUser());
                String id = "" + addCom.getId();
                String[] add = {id,"","","","","Y"};
                tableModel.addRow(add);
            }
        });

        delRowBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1)
                {
                    String id = (String) table.getValueAt(selectedRow,0);
                    Communication del = new Communication();
                    del.setId(Integer.parseInt(id));
                    dao.deleteCommunication(del,MainFrame.getUser());
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        saveBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (table.isEditing())
                {
                    table.getCellEditor().stopCellEditing();
                }
                if (checkInfo())
                {
                    Customer updateCust = new Customer(nameText.getText().trim(),
                            (String) sexComboBox.getSelectedItem(),
                            birthText.getText().trim(),
                            workUnitText.getText().trim(),
                            workAddressText.getText().trim(),
                            homeText.getText().trim(),
                            roleText.getText().trim());
                    updateCust.setId(cust.getId());
                    updateCust.setAvailable("Y");
                    dao.updateCustomer(updateCust, MainFrame.getUser());

                    int rowcount = table.getColumnCount();
                    for (int i = 0; i < rowcount; i++)
                    {
                        String id = (String) table.getValueAt(i, 0);
                        String officNum = (String) table.getValueAt(i, 1);
                        String mobileNum = (String) table.getValueAt(i, 2);
                        String emailStr = (String) table.getValueAt(i, 3);
                        String qqNum = (String) table.getValueAt(i, 4);
                        String available = (String) table.getValueAt(i, 5);

                        Communication updateCom = new Communication(cust, officNum, mobileNum, emailStr, qqNum);
                        updateCom.setId(Integer.parseInt(id));
                        updateCom.setAvailable(available);
                        dao.updateCommunication(updateCom, MainFrame.getUser());
                    }
                    JOptionPane.showMessageDialog(UpdateCustomerFrame.this, "保存成功");
                    dispose();
                }
            }
        });
        
        chckbxNewCheckBox.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if (chckbxNewCheckBox.isSelected())
                {
                    setTableModelSource(USABLESOUR);
                }
                else
                {
                    setTableModelSource(ALLSOUR);
                }
                table.setModel(tableModel);
            }
        });
    }

    private boolean checkInfo()
    {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        String name = nameText.getText();
        if ("".equals(name) || null == name)
        {
            result = false;
            sb.append("姓名不能为空！\n");
        }

        String sex = (String) sexComboBox.getSelectedItem();
        if ("".equals(sex) || null == sex)
        {
            result = false;
            sb.append("性别不能为空！\n");
        }

        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        String birth = birthText.getText();
        if (!(birth.matches(regex) || birth.equals("")))
        {
            result = false;
            sb.append("日期格式错误！\n");
        }

        int rowcount = tableModel.getRowCount();
        for (int i = 0; i < rowcount; i++)
        {
            String offic_num = (String) tableModel.getValueAt(i,1);
            String mobile_num = (String) tableModel.getValueAt(i,2);
            String email_str = (String) tableModel.getValueAt(i,3);
            String qq_num = (String) tableModel.getValueAt(i,4);
            String available = (String) tableModel.getValueAt(i,5);

            if (!(offic_num.matches("[0-9]+") || offic_num.equals("")))
            {
                result = false;
                sb.append("办公电话存在错误内容！\n");
            }

            if (!(mobile_num.matches("[0-9]+") || mobile_num.equals("")))
            {
                result = false;
                sb.append("移动电话存在错误内容！\n");
            }

            if (!(email_str.matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}")
                    || email_str.equals("")))
            {
                result = false;
                sb.append("电子邮件格式错误！\n");
            }

            if (!(qq_num.matches("[0-9]{5,10}") || qq_num.equals("")))
            {
                result = false;
                sb.append("QQ号码存在错误内容！\n");
            }

            if (!("Y".equals(available) || "N".equals(available)))
            {
                result = false;
                sb.append("信息有效性只能选择Y或N！\n");
            }
        }
        if (!result)
        {
            JOptionPane.showMessageDialog(null,sb.toString());
        }
        return result;
    }
}
