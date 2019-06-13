package com.atguigu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/*
 * 处理员工增删该查请求
 */
@Controller
public class EmployeeController {

	@Autowired
	// 自动装配service层业务逻辑组件
	EmployeeService employeeService;

	/*
	 * 单个批量二合一 批量删除:1-2-3 单个删除:1
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
	// 处理的请求带id的emp 即请求路径中带id
	public Msg deleteEmpById(@PathVariable("ids") String ids) {// 从请求路径中获取id
		if (ids.contains("-")) {
			// 多个删除的情况
			List<Integer> del_ids = new ArrayList();
			String[] str_ids = ids.split("-");
			// 组装id的集合

			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		} else {
			// 单个删除的情况
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value =
	 * "/emp/{id}",method=RequestMethod.DELETE)//处理的请求带id的emp 即请求路径中带id public
	 * Msg deleteEmpById(@PathVariable("id")Integer id){//从请求路径中获取id
	 * employeeService.deleteEmp(id); return Msg.success(); }
	 */

	/*
	 * 能支持直接发送PUT之类的请求还要封装请求体中的数据 配置HttpPutFormContentFilter过滤器 作用
	 * 将请求体中的数据包装成一个map request被重新包装 request.getParameter()被重写 就会从自己封装的map中取数据
	 * 员工更新
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		employeeService.updateEmp(employee);
		return Msg.success();
	}

	/*
	 * 根据id查询
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);

	}

	/*
	 * 检查用户名是否可用
	 */
	@ResponseBody
	@RequestMapping("/checkUser")
	public Msg checkUser(@RequestParam("empName") String empName) {// 使用注解@RequestParam明确的告诉springmvc要取出empName的值
		// 先判断用户名是否是合法的表达式
		String regex = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if (!empName.matches(regex)) {
			return Msg.fail().add("va_msg",
					"(来自后端的消息)用户名必须是6-16位数字和字母的组合或者2-5位的中文");// 状态200
		}

		// 数据库用户名重复校验
		boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();// 状态码100//客户端通过对状态码的识别来判断是否可用
		} else {
			return Msg.fail().add("va_msg", "用户名不可用");// 状态200
		}
	}

	/*
	 * 定义员工保存
	 */
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	// REST风格的uri
	@ResponseBody
	// BindingResult 封装校验结果
	// 实现自动封装 // @Valid 代表封装这个对象以后这个对象里面的数据要进行校验
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, Object> map = new HashMap();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名:" + fieldError.getField());
				System.out.println("错误信息:" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);// 校验失败，应该返回失败，在模态框中返回校验失败的错误信息
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/*
	 * @ResponseBody需要正常工作应导入的包： 负责将对象转换为json字符串 核心包 注解包 动态绑定
	 */
	// 此方法将员工的分页查询改造为ajax方法 返回json数据 里面包含分页信息和员工信息
	@RequestMapping("/emps")
	// 浏览器将会收到json返回的字符串
	@ResponseBody
	// 此注解可以把返回的对象转为json字符串
	// 页面发送请求的时候带上是要那页的数据
	public Msg getEmpsWithJson(
			@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			Model model) {
		// 引入PageHelper分页
		// 在查询之前只需要调用,传入页码，以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage后面紧跟的查询就是一个分页查询

		List<Employee> emps = employeeService.getAll();// 查询所有员工数据-->分页查询
		// 使用PageInfo包装查询后的结果,只需要将PageInfo交给页面
		// PageInfo种封装了详细的分页信息 包括我们查询出来的数据
		PageInfo page = new PageInfo(emps, 5);// 5为连续显示的页数
		return Msg.success().add("pageInfo", page);// 之前需要将对象放在request域中，现在直接返回即可
	}

	/*
	 * 查询员工数据(分页查询)
	 */
	// @RequestMapping("/emps")//处理的请求
	public String getEmps(
			@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			Model model) {
		// 引入PageHelper分页
		// 在查询之前只需要调用,传入页码，以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage后面紧跟的查询就是一个分页查询

		List<Employee> emps = employeeService.getAll();// 查询所有员工数据-->分页查询
		// 使用PageInfo包装查询后的结果,只需要将PageInfo交给页面
		// PageInfo种封装了详细的分页信息 包括我们查询出来的数据
		PageInfo page = new PageInfo(emps, 5);// 5为连续显示的页数
		model.addAttribute("pageInfo", page);
		// page.getNavigatepageNums();//拿到连续显示的页码
		return "list";// 通过视图解析器到list页面进行展示
	}

}
