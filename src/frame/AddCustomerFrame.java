package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import com.mr.contact.swing.CustomerFrame;
import pojo.*;
import com.mr.contact.dao.*;
import com.mr.contact.swing.ContactFrame;

public class AddCustomerFrame extends CustomerFrame
{
    private MainFrame frame;
    private DefaultTableModel tableModel;
    private JTable table;
    private Dao dao;
    private JTextField nameText;
    private JTextField workUnitText;
    private JTextField roleText;
    private JTextField workAddressText;
    private JTextField homeText;
    private JTextField birthText;
    private JButton cancelBtn;
    private JButton saveBtn;
    private JComboBox<String> sexCombo;
    private JButton addRowBtn;
    private JButton delRowBtn;

    public AddCustomerFrame(JFrame frame)
    {
        super(frame, CustomerFrame.ADD);
        this.frame = (MainFrame) frame;
        setTitle("添加客户资料");
        dao = DaoFactory.getDao();

        tableModel = getTableModel();
        table = getTable();
        nameText = getNameText();
        sexCombo = getSexCombo();
        birthText = getBirthText();
        workUnitText = getWorkUnitText();
        workAddressText = getWorkAddressText();
        homeText = getHomeText();
        roleText = getRoleText();
        JPanel btnPanel = new JPanel();

        btnPanel.setLayout(new GridLayout(1,2));

        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        FlowLayout p4layout = new FlowLayout();
        p4layout.setHgap(20);

        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.setLayout(p4layout);
        btnPanel.add(leftButtonPanel);

        delRowBtn = new JButton("删除行");
        leftButtonPanel.add(delRowBtn);

        addRowBtn = new JButton("添加行");
        leftButtonPanel.add(addRowBtn);

        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.setLayout(p4layout);
        btnPanel.add(rightButtonPanel);

        saveBtn = new JButton("保存");
        rightButtonPanel.add(saveBtn);

        cancelBtn = new JButton("关闭");
        rightButtonPanel.add(cancelBtn);

        addAction();
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

        delRowBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectRow = table.getSelectedRow();
                if (selectRow != -1)
                {
                    tableModel.removeRow(selectRow);
                }
            }
        });

        addRowBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String[] add = {"","","",""};
                tableModel.addRow(add);
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
                    Customer updateCustomer = new Customer(nameText.getText().trim(),
                            (String) sexCombo.getSelectedItem(),
                            birthText.getText().trim(),
                            workUnitText.getText().trim(),
                            workAddressText.getText().trim(),
                            homeText.getText().trim(),
                            roleText.getText().trim());
                    dao.addCustomer(updateCustomer,MainFrame.getUser());
                    int rowcount = table.getRowCount();
                    for (int i = 0; i < rowcount; i++)
                    {


                        String offic_num = (String) table.getValueAt(i,1);

                        String mobile_num = (String) table.getValueAt(i,2);

                        String email_str = (String) table.getValueAt(i,3);

                        String qq_num = (String) table.getValueAt(i,4);

                        Communication updateCom = new Communication(updateCustomer,
                                offic_num,mobile_num,email_str,qq_num);

                        dao.addCommunication(updateCom,MainFrame.getUser());
                    }

                    JOptionPane.showMessageDialog(AddCustomerFrame.this,"添加成功");
                    dispose();
                }
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

        String sex = (String) sexCombo.getSelectedItem();
        if ("".equals(sex) || null == sex)
        {
            result = false;
            sb.append("性别不能为空！\n");
        }

        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        String birth = birthText.getText();
        if (!birth.matches(regex))
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

            if (!offic_num.matches("[0-9]+") && !offic_num.equals(""))
            {
                result = false;
                sb.append("办公电话存在错误内容！\n");
            }

            if (!mobile_num.matches("[0-9]+") && !mobile_num.equals(""))
            {
                result = false;
                sb.append("移动电话存在错误内容！\n");
            }

            if (!email_str.matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}")
                    && !email_str.equals(""))
            {
                result = false;
                sb.append("电子邮件格式错误！\n");
            }

            if (!qq_num.matches("[0-9]{5,10}") && !qq_num.equals(""))
            {
                result = false;
                sb.append("QQ号码存在错误内容！\n");
            }
        }
        if (!result)
        {
            JOptionPane.showMessageDialog(null,sb.toString());
        }
        return result;
    }

    public void dispose()
    {
        super.dispose();
        frame.initTable();
    }
}
