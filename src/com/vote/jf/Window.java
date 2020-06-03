package com.vote.jf;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Workbook;

import com.vote.model.User;
import com.vote.utils.ExcelUtils;

public class Window {

	//WIDTH是指整个顶层框架的宽度。
	//HEIGHT是指整个顶层框架的长度。
	static final int WIDTH=400;
	static final int HEIGHT=350;


	public static void main(String[] args) throws Exception{
		JFrame jf=new JFrame("自动投票");
		jf.setLayout(new BorderLayout());
		jf.setSize(WIDTH,HEIGHT);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		JPanel contentPane=new JPanel();//创建六个中间容器，并且将contentPane放到顶层容器内
		JPanel p1=new JPanel();
		jf.add(p1);
		placeComponents(jf,p1);

	}



	private static void placeComponents(JFrame jf,JPanel panel) throws Exception {

		/* 布局部分我们这边不多做介绍
		 * 这边设置布局为 null
		 */
		panel.setLayout(null);

		// 创建 JLabel
		JLabel userLabel = new JLabel("Excel文件：");
		/* 这个方法定义了组件的位置。
		 * setBounds(x, y, width, height)
		 * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
		 */
		userLabel.setBounds(10,20,80,25);
		panel.add(userLabel);

		/*
		 * 创建文本域用于用户输入
		 */
		JButton userText = new JButton("选择文件");
		userText.setBounds(100,20,80,25);
		panel.add(userText);

		// 输入密码的文本域
		JLabel passwordLabel = new JLabel("投票地址：");
		passwordLabel.setBounds(10,50,80,25);
		panel.add(passwordLabel);

		/*
		 *这个类似用于输入的文本域
		 * 但是输入的信息会以点号代替，用于包含密码的安全性
		 */
		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100,50,165,25);
		panel.add(passwordText);






		// 输入投票按钮
		JLabel voteGroupName = new JLabel("投票分组:");
		voteGroupName.setBounds(10,80,80,25);
		panel.add(voteGroupName);

		/*
		 *这个类似用于输入的文本域
		 * 但是输入的信息会以点号代替，用于包含密码的安全性
		 */
		JPasswordField voteGroupNameText = new JPasswordField(20);
		voteGroupNameText.setBounds(100,80,165,25);
		panel.add(voteGroupNameText);




		// 输入投票按钮
		JLabel voteName = new JLabel("投票按钮名称:");
		voteName.setBounds(10,110,80,25);
		panel.add(voteName);

		/*
		 *这个类似用于输入的文本域
		 * 但是输入的信息会以点号代替，用于包含密码的安全性
		 */
		JPasswordField voteNameText = new JPasswordField(20);
		voteNameText.setBounds(110,110,165,25);
		panel.add(voteNameText);


		// 创建登录按钮
		JButton loginButton = new JButton("运行");
		loginButton.setBounds(10, 130, 80, 25);
		panel.add(loginButton);


		JFileChooser fileChooser = new JFileChooser();
		createUI(jf,userText,fileChooser);
		getFile(loginButton,fileChooser);


	}

	//运行
	private static void getFile(JButton loginButton,JFileChooser fileChooser) throws Exception{
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					getFile(fileChooser);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}





	//获取文件内容
	private static List<User> getFile(JFileChooser fileChooser) throws Exception{

		String filename = fileChooser.getSelectedFile().getPath();
		File file=new File(filename);

		if(!ExcelUtils.isExcel(filename)){
			throw new Exception("文件格式错误");
		}
		InputStream in = new FileInputStream(filename);
		System.out.println(filename+"-----------");
		Workbook wk=ExcelUtils.getWorkbook(in, filename);
		ExcelUtils excelUtils=new ExcelUtils(wk);


		//对读取的数据进行操作

		List<User> userlist=new ArrayList();
		List<List<String>> list = excelUtils.read();

		if (list != null && list.size() > 0) {

			for(List<String> item:list){

				User user=new User();
				user.setUsername(item.get(0));
				user.setPassword(item.get(1));
				userlist.add(user);

				System.out.println(user.getUsername()+"--------------------------");
			}
		}

		return userlist;

	}


	//读取文件面板
	private static  void createUI(JFrame jf,JButton button,JFileChooser fileChooser){



		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		panel.setLayout(layout);

		final JLabel label = new JLabel();

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int option = fileChooser.showOpenDialog(jf);
				if(option == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					label.setText("选择文件是: " + file.getName());
				}else{
					label.setText("打开命令取消");
				}
			}
		});

		panel.add(button);
		panel.add(label);
		jf.getContentPane().add(panel, BorderLayout.CENTER);

	}


}
