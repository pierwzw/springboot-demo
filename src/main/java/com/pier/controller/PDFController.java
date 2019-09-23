package com.pier.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.pier.bean.User;
import com.pier.service.PdfExportService;
import com.pier.service.UserService;
import com.pier.view.PdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.util.List;

/**** imports ****/
@Controller
@RequestMapping("/user")
public class PDFController {
	// 注入用户服务类
	@Autowired
	private UserService userService = null;

	// 导出接口
	@GetMapping("/export/pdf")
	public ModelAndView exportPdf(String account) {
		// 查询用户信息列表
		List<User> userList = userService.findUsersByAccount(Double.valueOf(account));
		// 定义PDF视图
		View view = new PdfView(exportService());
		ModelAndView mv = new ModelAndView();
		// 设置视图
		mv.setView(view);
		// 加入数据模型
		mv.addObject("userList", userList);
		return mv;
	}

	// 导出PDF自定义
	@SuppressWarnings("unchecked")
	private PdfExportService exportService() {
		// 使用Lambda表达式定义自定义导出
		return (model, document, writer, request, response) -> {
			try {
				// A4纸张
				document.setPageSize(PageSize.A4);
				// 标题
				document.addTitle("用户信息");
				// 换行
				document.add(new Chunk("\n"));
				// 表格，3列
				PdfPTable table = new PdfPTable(3);
				// 单元格
				PdfPCell cell = null;
				// 字体，定义为蓝色加粗
				Font f8 = new Font();
				f8.setColor(Color.BLUE);
				f8.setStyle(Font.BOLD);
				// 标题
				cell = new PdfPCell(new Paragraph("id", f8));
				// 居中对齐
				cell.setHorizontalAlignment(1);
				// 将单元格加入表格
				table.addCell(cell);
				cell = new PdfPCell(new Paragraph("user_name", f8));
				// 居中对齐
				cell.setHorizontalAlignment(1);
				table.addCell(cell);
				cell = new PdfPCell(new Paragraph("account", f8));
				cell.setHorizontalAlignment(1);
				table.addCell(cell);
				// 获取数据模型中的用户列表
				List<User> userList = (List<User>) model.get("userList");
				for (User user : userList) {
					document.add(new Chunk("\n"));
					cell = new PdfPCell(new Paragraph(user.getId() + ""));
					table.addCell(cell);
					cell = new PdfPCell(new Paragraph(user.getUsername()));
					table.addCell(cell);
					float account = (float) user.getAccount();
					cell = new PdfPCell(new Paragraph(""+account));
					table.addCell(cell);
				}
				// 在文档中加入表格
				document.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		};
	}

//	// 显示用户
//	@GetMapping("/show")
//	public String showUser(Long id, Model model) {
//	    User user = userService.getUser(id);
//	    model.addAttribute("user", user);
//	    return "data/user";
//	}
//
//	// 使用字符串指定跳转
//	@GetMapping("/redirect1")
//	public String redirect1(String userName, String note) {
//	    User user = new User();
//	    user.setNote(note);
//	    user.setUserName(userName);
//	    // 插入数据库后，回填user的id
//	    userService.insertUser(user);
//	    return "redirect:/user/show?id=" + user.getId();
//	}
//
//	// 使用模型和视图指定跳转
//	@GetMapping("/redirect2")
//	public ModelAndView redirect2(String userName, String note) {
//	    User user = new User();
//	    user.setNote(note);
//	    user.setUserName(userName);
//	    userService.insertUser(user);
//	    ModelAndView mv = new ModelAndView();
//	    mv.setViewName("redirect:/user/show?id=" + user.getId());
//	    return mv;
//	}

	// 显示用户
	// 参数user直接从数据模型RedirectAttributes对象中取出
	@RequestMapping("/showUser")
	public String showUser(User user, Model model) {
	    System.out.println(user.getId());
	    return "data/user";
	}

	// 使用字符串指定跳转
	@RequestMapping("/redirect1")
	public String redirect1(String userName, String password, RedirectAttributes ra) {
	    User user = new User();
	    user.setPassword(password);
	    user.setUsername(userName);
	    userService.insertUser(user);
	    // 保存需要传递给重定向的对象
	    ra.addFlashAttribute("user", user);
	    return "redirect:/user/showUser";
	}

	// 使用模型和视图指定跳转
	@RequestMapping("/redirect2")
	public ModelAndView redirect2(String userName, String password,
                                  RedirectAttributes ra) {
	    User user = new User();
	    user.setPassword(password);
	    user.setUsername(userName);
	    userService.insertUser(user);
	    // 保存需要传递给重定向的对象
	    ra.addFlashAttribute("user", user);
	    ModelAndView mv = new ModelAndView();
	    mv.setViewName("redirect:/user/showUser");
	    return mv;
	}

	@GetMapping("/header/page")
	public String headerPage() {
	    return "header";
	}

	@PostMapping("/header/user")
	@ResponseBody
	// 通过@RequestHeader接收请求头参数
	public User headerUser(@RequestHeader("id") Long id) {
	    //User user = userService.getUser(id);
	    //return user;
		return null;
	}
}