package com.atguigu.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.DepartmentService;

/*
 * 处理和部门有关的请求
 */
@Controller//是一个控制器
public class DepartmentController {
	
	@Autowired//自动装配
	private DepartmentService departmentService;
	
	/*
	 * 返回所有的部门信息
	 */
	@RequestMapping("/depts")//处理的请求是depts
	@ResponseBody
	public Msg getDepts(){//所有json数据的返回都包装为msg对象
		//查出的所有部门信息
		List<Department> list = departmentService.getDepts();
		return Msg.success().add("depts", list);
	}

}
