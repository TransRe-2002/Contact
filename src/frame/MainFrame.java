package frame;

import com.mr.contact.swing.ContactFrame;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.mr.contact.dao.Dao;
import com.mr.contact.dao.DaoFactory;
import com.mr.contact.swing.FixedTable;
import pojo.Customer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import pojo.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 主窗体
 *
 */
public class MainFrame extends ContactFrame // 继承ContactFrame类
{
    private Dao dao;
    private FixedTable table;
    private DefaultTableModel tableModel;
    static private User user;
    private JButton update_btn;
    private JButton add_btn;
    private JButton del_btn;
    /**
     *
     * 构造方法
     */
    public MainFrame()
    {
        setTitle("通讯录系统");// 窗体标题
        init();
        validate();
        addAction();
    }// MainFrame()结束

    private void addAction()
    {
        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {

                    String id = (String) table.getValueAt(table.getSelectedRow(),0);
                    Customer cust = dao.selectCustomer(Integer.parseInt(id));
                    cust.setId(Integer.parseInt(id));
                    ShowInfoFrame info = new ShowInfoFrame(cust,MainFrame.this);
                    info.setVisible(true);
                }
            }
        });

        update_btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int rowindex = table.getSelectedRow();
                if (rowindex > -1)
                {
                    String id = (String) table.getValueAt(rowindex,0);

                    Customer update = dao.selectCustomer(Integer.parseInt(id));

                    UpdateCustomerFrame show = new UpdateCustomerFrame(update, MainFrame.this);
                    show.setVisible(true);
                }
            }
        });

        add_btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                AddCustomerFrame add = new AddCustomerFrame(MainFrame.this);
                add.setVisible(true);
            }
        });

        del_btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int rowindex = table.getSelectedRow();
                if (rowindex > -1)
                {
                    int i = JOptionPane.showConfirmDialog(MainFrame.this,
                            "确认是否删除"+table.getValueAt(rowindex,1) + "？",
                            "注意！",JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION)
                    {
                        Customer del = new Customer();
                        String id = (String) table.getValueAt(rowindex,0);
                        del.setId(Integer.parseInt(id));
                        dao.deleteCustomer(del,user);
                        tableModel.removeRow(table.getSelectedRow());
                    }
                }

            }
        });
    }

    protected void init()
    {
        super.init();
        dao = DaoFactory.getDao();
        table = getTable();
        table.setCellEditable(false);
        initTable();
        update_btn = new JButton("修改");
        add_btn = new JButton("添加");
        del_btn = new JButton("删除");
        if (user.getStatus().equals(User.ADMIN))
        {
            JPanel bottomPanel = new JPanel();
            FlowLayout bottomLaybot = new FlowLayout();
            bottomLaybot.setHgap(20);
            bottomLaybot.setAlignment(FlowLayout.RIGHT);
            bottomPanel.setLayout(bottomLaybot);
            bottomPanel.add(update_btn);
            bottomPanel.add(add_btn);
            bottomPanel.add(del_btn);
            getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        }
    }

    public void initTable()
    {
        tableModel = getUsableModleSoure();
        table.setModel(tableModel);
    }

    private DefaultTableModel getUsableModleSoure()
    {
        List<Customer> usableList = dao.selectUsableCustomer();
        return assembledModleSoure(usableList);
    }

    private DefaultTableModel assembledModleSoure(List<Customer> usableList)
    {
        int customerCount = usableList.size();
        String[] columnNames = {"编号","姓名","性别","出生日期","工作单位","职位","工作地点","家庭住址"};
        String[][] tableValues = new String[customerCount][8];
        for (int i = 0; i < customerCount; i++)
        {
            Customer cust = usableList.get(i);
            tableValues[i][0] = "" + cust.getId();
            tableValues[i][1] = cust.getName();
            tableValues[i][2] = cust.getSex();
            tableValues[i][3] = cust.getBirth();
            tableValues[i][4] = cust.getWork_unit();
            tableValues[i][5] = cust.getRole();
            tableValues[i][6] = cust.getWork_addr();
            tableValues[i][7] = cust.getHome_addr();
        }
        DefaultTableModel tmp = new DefaultTableModel(tableValues,columnNames);
        return tmp;
    }

    static public User getUser()
    {
        return user;
    }

    static public void setUser(User user)
    {
        MainFrame.user = user;
    }
}// MainFrame类结束
