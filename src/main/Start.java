package main;

import frame.LoginFrame;

/**
 * 程序运行入口类
 */
public class Start
{
    public static void main(String[] args)
    {
        try// 启用BeautyEye主题
        {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        LoginFrame frame = new LoginFrame();// 登陆窗体
        frame.setVisible(true);// 窗体可见
    }
}
