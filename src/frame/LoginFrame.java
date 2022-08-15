package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import pojo.User;
import com.mr.contact.dao.*;

public class LoginFrame extends JFrame
{
    public LoginFrame()
    {
        setTitle("通讯录登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(310,210);
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension d = tool.getScreenSize();
        setLocation((d.width - getWidth())/2,(d.height - getHeight())/2);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(2,1));
        contentPane.add(centerPanel,BorderLayout.CENTER);

        FlowLayout centerLayout = new FlowLayout();
        centerLayout.setHgap(10);

        JPanel aFloorPanel = new JPanel(centerLayout);
        centerPanel.add(aFloorPanel);
        JLabel usernameLabel = new JLabel("账号：");

        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aFloorPanel.add(usernameLabel);

        final JTextField usernameField = new JTextField();
        usernameField.setColumns(20);
        aFloorPanel.add(usernameField);

        JPanel bFloorPanel = new JPanel(centerLayout);
        centerPanel.add(bFloorPanel);
        JLabel pwdLabel = new JLabel("密码：");
        pwdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bFloorPanel.add(pwdLabel);

        final JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(20);
        bFloorPanel.add(passwordField);

        JPanel southPanel = new JPanel(centerLayout);
        contentPane.add(southPanel,BorderLayout.SOUTH);

        final JButton loginBtn = new JButton("登录");
        southPanel.add(loginBtn);

        JButton closeBtn = new JButton("关闭");
        southPanel.add(closeBtn);

        closeBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        loginBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Dao dao = DaoFactory.getDao();
                String account = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                User user = dao.selectUser(account,password);
                if (null == user)
                {
                    JOptionPane.showMessageDialog(null,"您输入的账号密码不正确！");
                }
                else
                {
                    MainFrame.setUser(user);
                    MainFrame frame = new MainFrame();
                    dispose();
                }
            }
        });

        passwordField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loginBtn.doClick();
            }
        });

    }
}
