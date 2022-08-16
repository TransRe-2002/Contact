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
 */
public class MainFrame extends ContactFrame // 继承ContactFrame类
{
    private Dao dao;// 数据库接口
    private FixedTable table;// 通讯录表格
    private DefaultTableModel tableModel;// 定义表格模型对象
    static private User user;// 当前登录用户
    private JButton update_btn;// 修改按钮
    private JButton add_btn;// 添加按钮
    private JButton del_btn;// 删除按钮

    /**
     * 构造方法
     */
    public MainFrame()
    {
        setTitle("通讯录系统");// 窗体标题
        init();// 组件初始化
        validate();// 重新加载组件
        addAction();// 开启组件监听
    }// MainFrame()结束

    /**
     * 添加组件监听
     */
    private void addAction()
    {
        table.addMouseListener(new MouseAdapter()// 表格添加鼠标事件监听
        {
            @Override
            public void mouseClicked(MouseEvent e)// 当鼠标点击时
            {
                if (e.getClickCount() == 2)// 如果鼠标是双击事件
                {
                    // 获得选中行的第一列数据，赋值为id
                    String id = (String) table.getValueAt(table.getSelectedRow(),0);
                    Customer cust = dao.selectCustomer(Integer.parseInt(id));// 获取此id的持久化客户信息对象
                    cust.setId(Integer.parseInt(id));// 将id值转成int值并付给客户对象
                    ShowInfoFrame info = new ShowInfoFrame(cust,MainFrame.this);// 打开详细信息展示窗口
                    info.setVisible(true);// 窗口可见
                }// if结束
            }// mouseClicked()结束
        });// addMouseListener()结束

        update_btn.addActionListener(new ActionListener()// 更新按钮添加时间监听
        {
            @Override
            public void actionPerformed(ActionEvent e)// 当点击时
            {
                int rowindex = table.getSelectedRow();// 获取当前表格选中的行索引
                if (rowindex > -1)// 如果有选中的值
                {
                    String id = (String) table.getValueAt(rowindex,0);// 获取表格第一列的值作为id
                    // 获取数据库中与此id相同的客户数据
                    Customer update = dao.selectCustomer(Integer.parseInt(id));
                    // 创建客户修改窗口
                    UpdateCustomerFrame show = new UpdateCustomerFrame(update, MainFrame.this);
                    show.setVisible(true);// 打客户修改窗口可见
                } // if结束
            }// actionPerformed()结束
        });// update_btn.addActionListener()结束

        add_btn.addActionListener(new ActionListener()// 添加按钮添加动作监听
        {
            @Override
            public void actionPerformed(ActionEvent e)// 当点击时
            {
                // 创建添加客户窗口
                AddCustomerFrame add = new AddCustomerFrame(MainFrame.this);
                add.setVisible(true);// 添加客户窗口可见
            }// actionPerformed()结束
        });// add_btn.addActionListener()结束

        del_btn.addActionListener(new ActionListener()// 删除按钮添加事件监听
        {
            @Override
            public void actionPerformed(ActionEvent e)// 当点击时
            {
                int rowindex = table.getSelectedRow();// 获取当前表格选中的行索引
                if (rowindex > -1)// 如果有选中的值
                {
                    // 弹出提示对话框，提示客户名字，获取用户操作返回的结果
                    int i = JOptionPane.showConfirmDialog(MainFrame.this,
                            "确认是否删除"+table.getValueAt(rowindex,1) + "？",
                            "注意！",JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION)// 如果选择yes
                    {
                        Customer del = new Customer();// 创建客户对象
                        String id = (String) table.getValueAt(rowindex,0);// 获取被删除的客户id
                        del.setId(Integer.parseInt(id));// 设置客户id
                        dao.deleteCustomer(del,user);// 将客户信息删除
                        tableModel.removeRow(table.getSelectedRow());// 表格删除选中行此行
                    }// if结束
                }// if结束
            }// actionPerformed()结束
        });// del_btn.addActionListener()结束
    }// addAction()结束

    /**
     * 组件初始化
     */
    protected void init()
    {
        super.init();// 调用父类init()方法
        dao = DaoFactory.getDao();// 实例化数据库接口
        table = getTable();// 创建指定表格模型的表格
        table.setCellEditable(false);// 让表格不可编辑
        initTable();// 初始化表格数据

        update_btn = new JButton("修改");// 修改按钮
        add_btn = new JButton("添加");// 添加按钮
        del_btn = new JButton("删除");// 删除按钮
        if (user.getStatus().equals(User.ADMIN))// 如果是管理员身份
        {
            JPanel bottomPanel = new JPanel();// 创建底部按钮面板
            FlowLayout bottomLaybot = new FlowLayout();// 创建流布局
            bottomLaybot.setHgap(20);// 横间距20像素
            bottomLaybot.setAlignment(FlowLayout.RIGHT);// 右对齐
            bottomPanel.setLayout(bottomLaybot);// 载入流布局
            bottomPanel.add(update_btn);// 底部面板添加按钮
            bottomPanel.add(add_btn);// 底部面板添加按钮
            bottomPanel.add(del_btn);// 底部面板添加按钮
            getContentPane().add(bottomPanel,BorderLayout.SOUTH);// 添加底部面板放到主容器最南位置
        }// if结束
    }// init()结束

    /**
     * 初始化表格数据
     */
    public void initTable()
    {
        tableModel = getUsableModleSoure();// 获取所有有效客户信息
        table.setModel(tableModel);// 客户信息表格加载数据模型
    }// initTable()

    /**
     * 查询所有有效客户信息
     *
     * @return 表格数据模型
     */
    private DefaultTableModel getUsableModleSoure()
    {
        List<Customer> usableList = dao.selectUsableCustomer();// 获取所有有效客户
        return assembledModleSoure(usableList);// 返回有所有有效客户表格数据模型
    }// getUsableModleSoure()结束

    /**
     * 根据不同的客户集合，获取相应的表格数据模型
     *
     * @param usableList
     *            - 客户集合
     * @return 表格数据模型
     */
    private DefaultTableModel assembledModleSoure(List<Customer> usableList)
    {
        int customerCount = usableList.size();// 获取集合的客户数量
        String[] columnNames = {"编号","姓名","性别","出生日期","工作单位","职位","工作地点","家庭住址"};// 定义表格列名数组
        String[][] tableValues = new String[customerCount][8];// 创建表格数据数组
        for (int i = 0; i < customerCount; i++)// 遍历表格所有行
        {
            Customer cust = usableList.get(i);// 获取行用户对象
            tableValues[i][0] = "" + cust.getId();// 第一列为编号
            tableValues[i][1] = cust.getName();// 第二列为名称
            tableValues[i][2] = cust.getSex();// 第三列为性别
            tableValues[i][3] = cust.getBirth();// 第四列为出生日期
            tableValues[i][4] = cust.getWork_unit();// 第五列为公司名称
            tableValues[i][5] = cust.getRole();// 第六位为职位
            tableValues[i][6] = cust.getWork_addr();// 第七列为公司地址
            tableValues[i][7] = cust.getHome_addr();// 第八列为家庭住址
        }// for结束
        // 根据列名数组和数据数组创建表格数据模型
        DefaultTableModel tmp = new DefaultTableModel(tableValues,columnNames);
        return tmp;
    }// assembledModleSoure()结束

    /**
     * 获取当前操作用户
     */
    static public User getUser()
    {
        return user;
    }// getUser()结束

    /**
     * 设置当前操作用户
     *
     * @param user
     *            - 当前操作用户
     */
    static public void setUser(User user)
    {
        MainFrame.user = user;
    }// setUser()结束
}// MainFrame类结束
