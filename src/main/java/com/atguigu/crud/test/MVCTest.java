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
 * ʹ��spring����ģ���ṩ�������ܣ�����crud�������ȷ��
 * spring4���Ե�ʱ����Ҫservlet3.0de֧��

 @RunWith(SpringJUnit4ClassRunner.class)//��˭�ĵ�Ԫ����
 @WebAppConfiguration//ioc��������
 @ContextConfiguration(locations={"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
 public class MVCTest {

 //����springmvc��ioc
 @Autowired//��ȡioc�����ڲ���
 WebApplicationContext context;

 //����mvc���󣬻�ȡ��������-->��������������ȷ��
 MockMvc mockMvc;

 @Before//ÿ����֮ǰ��Ҫ���г�ʼ��
 public void initMokcMvc(){
 mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
 }

 @Test
 //��д���Է�ҳ�ķ���
 public void testPage() throws Exception{
 //ģ�������õ�����ֵ
 MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "1")).andReturn();

 //����ɹ��Ժ��������л���pageInfo;���ǿ���ȡ��pageinfo������֤
 MockHttpServletRequest request = result.getRequest();//�õ�����
 PageInfo pi = (PageInfo) request.getAttribute("pageInfo");
 System.out.println("��ǰҳ�룺" + pi.getPageNum());//�õ���ǰҳ��
 System.out.println("��ҳ�룺" + pi.getPages());//��ҳ��
 System.out.println("�ܼ�¼����" + pi.getTotal());//�ܼ�¼��
 System.out.println("��ҳ����Ҫ������ʾ��ҳ�룺");
 int[] nums = pi.getNavigatepageNums();
 for(int i:nums){
 System.out.print(" "+i);
 }

 //��ȡԱ������
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
		// ����ɹ�������������pageInfo����
		MockHttpServletRequest request = result.getRequest();
		PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
		System.out.println("��ǰҳ�룺" + pageInfo.getPageNum());// �õ���ǰҳ��
		System.out.println("��ҳ�룺" + pageInfo.getPages());// ��ҳ��
		System.out.println("�ܼ�¼����" + pageInfo.getTotal());// �ܼ�¼��
		System.out.println("��ҳ����Ҫ������ʾ��ҳ�룺");
		int[] nums = pageInfo.getNavigatepageNums();
		for (int i : nums) {
			System.out.print(" " + i);
		}
		System.out.println();

		// ��ȡԱ������
		List<Employee> list = pageInfo.getList();
		for (Employee employee : list) {
			System.out.println("ID" + employee.getEmpId() + "==>Name:"
					+ employee.getEmpName());
		}
		
	}
}
