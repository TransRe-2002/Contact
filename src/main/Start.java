package main;

import frame.LoginFrame;

public class Start
{
    public static void main(String[] args)
    {
        try
        {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }
}
