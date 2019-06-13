package com.atguigu.crud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.atguigu.crud.bean.Employee;
import com.github.pagehelper.PageInfo;

/*
 * 使用spring测试模块提供的请求功能：测试crud请求的正确性
 * spring4测试的时候需要servlet3.0de支持

 @RunWith(SpringJUnit4ClassRunner.class)//用谁的单元测试
 @WebAppConfiguration//ioc容器本身
 @ContextConfiguration(locations={"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
 public class MVCTest {

 //传入springmvc的ioc
 @Autowired//获取ioc容器内部的
 WebApplicationContext context;

 //虚拟mvc请求，获取到处理结果-->进而检验结果的正确性
 MockMvc mockMvc;

 @Before//每次用之前都要进行初始化
 public void initMokcMvc(){
 mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
 }

 @Test
 //编写测试分页的方法
 public void testPage() throws Exception{
 //模拟请求拿到返回值
 MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "1")).andReturn();

 //请求成功以后，请求域中会有pageInfo;我们可以取出pageinfo进行验证
 MockHttpServletRequest request = result.getRequest();//拿到请求
 PageInfo pi = (PageInfo) request.getAttribute("pageInfo");
 System.out.println("当前页码：" + pi.getPageNum());//拿到当前页码
 System.out.println("总页码：" + pi.getPages());//总页码
 System.out.println("总记录数：" + pi.getTotal());//总记录数
 System.out.println("在页面需要连续显示的页码：");
 int[] nums = pi.getNavigatepageNums();
 for(int i:nums){
 System.out.print(" "+i);
 }

 //获取员工数据
 List<Employee> list = pi.getList();
 for(Employee employee:list){
 System.out.print("ID" + employee.getEmpId() + "==>Name:" + employee.getEmpName());
 }


 }


 }
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring.xml",
		"classpath:springmvc.xml" })
public class MVCTest {
	@Autowired
	WebApplicationContext context;
	MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testPage() throws Exception {
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/emps").param("pn", "5"))
				.andReturn();
		// 请求成功后请求域中有pageInfo对象
		MockHttpServletRequest request = result.getRequest();
		PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
		System.out.println("当前页码：" + pageInfo.getPageNum());// 拿到当前页码
		System.out.println("总页码：" + pageInfo.getPages());// 总页码
		System.out.println("总记录数：" + pageInfo.getTotal());// 总记录数
		System.out.println("在页面需要连续显示的页码：");
		int[] nums = pageInfo.getNavigatepageNums();
		for (int i : nums) {
			System.out.print(" " + i);
		}
		System.out.println();

		// 获取员工数据
		List<Employee> list = pageInfo.getList();
		for (Employee employee : list) {
			System.out.println("ID" + employee.getEmpId() + "==>Name:"
					+ employee.getEmpName());
		}
		
	}
}
